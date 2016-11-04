package com.example.myfirst.myapplication;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import static org.opencv.core.Core.*;
import static org.opencv.highgui.Highgui.*;
import static org.opencv.imgproc.Imgproc.*;

/* Supplies the methods to perform the image processing required to gather color information of the
 * individual tests on the card. */
public class ImageProcAct {
    private static final double PERC1 = 0.015;
    private static final double PERC2 = 0.01;

    /* Takes in the image taken by the camera and converts it to a Mat that can then be used to
     * perform the image processing needed.
     * Parameters:
     *      String fileName: name of the picture file to perform the processing on.
     */
    public static void readImage(String fileName) {
        System.loadLibrary(NATIVE_LIBRARY_NAME);

        /* loads the image into two separate Mats, the original and the editable. The editable image
         * is stored as a grayscale image. */
        Mat originalImg = imread(fileName);
        Mat grayScaleImg = imread(fileName, CV_LOAD_IMAGE_GRAYSCALE);

        /* Thresholds the image to create a binary image */
        threshold(grayScaleImg, grayScaleImg, 128, 255, THRESH_BINARY);

        List<Rect> testSquares = new ArrayList<Rect>();
        List<Scalar> testColors = new ArrayList<Scalar>();
        testSquares = findTestSquares(grayScaleImg);
        testSquares = sortTestSquares(testSquares);
        testColors = findColor(originalImg, testSquares);
    }

    /* Finds all the contours in the image, then removes any non-square contours and contours that
     * are the incorrect size range.
     * Parameters:
     *      Mat img: the grayscale Mat of an image, allowing us to find the contours and narrow them
     *               down to the ones we want.
     * Returns:
     *      List<Rect>: holds the 12 test rectangles that need further processing.
     */
    private static List<Rect> findTestSquares(Mat img) {
        /* Uses findContours to find all contours, or closed shapes, in the image. */
        List<MatOfPoint> allContours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> properContours = new ArrayList<MatOfPoint>();
        findContours(img, allContours, new Mat(), RETR_LIST, CHAIN_APPROX_SIMPLE);

        /* Removes all none-square and improperly sized contours from the list, leaving only
         * the outside and inside contours of each test square. */
        for(int i = 0; i < allContours.size(); i++) {
            if(contourArea(allContours.get(i)) > img.width() * img.height() * PERC2
            && contourArea(allContours.get(i)) < img.width() * img.height() * PERC1) {
                MatOfPoint2f tempMat = new MatOfPoint2f(allContours.get(i).toArray());
                MatOfPoint2f polyMat = new MatOfPoint2f();
                approxPolyDP(tempMat, polyMat, 10, true);
                if(polyMat.total() == 4) {
                    properContours.add(allContours.get(i));
                }
            }
        }

        /* Converts the contours into Rects to further remove squares that are inside other squares,
         * therefore grabbing only the external contours of each test square. */
        List<Rect> testRects = new ArrayList<Rect>();
        for(int i = 0; i < properContours.size(); i++) {
            testRects.add(boundingRect(properContours.get(i)));
        }
        /* Removes all internal squares to other squares */
        for(int i = 0; i < testRects.size(); i++) {
            for(int j = i+1; j < testRects.size(); j++) {
                if((testRects.get(i).x >= testRects.get(j).x && testRects.get(i).x < testRects.get(j).x + testRects.get(j).width) &&
                (testRects.get(i).y >= testRects.get(j).y && testRects.get(i).x < testRects.get(j).x + testRects.get(j).height)) {
                    testRects.remove(i);
                    j = i+1;
                }
            }
        }
        return testRects;
    }

    /* Sorts the list of rectangles in order from upper left to bottom right.
     * Parameters:
     *      List<Rect> squares: The list of Rects to sort, should only contain 12 squares
     * Returns:
     *      List<Rect>: holds the sorted squares.
     */
    private static List<Rect> sortTestSquares(List<Rect> squares) {
        List<Rect> initSort = new ArrayList<Rect>();
        Rect next = new Rect();
        int size = squares.size();

        /* Runs through the list of squares and pulls the highest square (lowest y value) out from
         * the current list and puts it in a new list. This sorts all of the squares by y, but will
         * still need sorted by x. Overall, this puts the first column as the first 3, second column
         * as the second three and so forth. */
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < squares.size(); j++) {
                if (next.y > squares.get(j).y || j == 0) {
                    next = squares.get(j);
                }
            }
            initSort.add(next);
            squares.remove(next);
            next = new Rect();
        }
        /* This will sort each set of three by their x value (lowest to the left, highest on right).
         * This will complete the overall sort, giving us a top-to-bottom, left-to-right sort. */
        int moves = 0;
        for(int i = 0; i < 4; i++) {
            do {
                for(int j = i*3; j < (i+1)*3; j++) {
                    if(j+1 < (i+1)*3 && initSort.get(j).x > initSort.get(j+1).x) {
                        next = initSort.get(j);
                        initSort.set(j, initSort.get(j+1));
                        initSort.set(j+1, next);
                        moves++;
                    }
                }
            } while(moves > 0);
        }
        return initSort;
    }

    /* Finds the colors inside each square by first shrinking the Rect object for the square to be
     * inside the square on the test card. It then finds the average color in each square and stores
     * it as a Scalar in BGR colorspace.
     * Parameters:
     *      Mat img: the original, color image that was grabbed initially
     *      List<Rect> squares: the sorted list of the test squares
     * Returns:
     *      List<Scalar>: holds the average color of each square in the same sort as the squares in
     *                    the list. It is stored in the BGRA colorspace (A will sometimes be 0
     *                    depending on if the initial image has an alpha)
     */
    private static List<Scalar> findColor(Mat img, List<Rect> squares) {
        List<Scalar> colors = new ArrayList<Scalar>();
        /* This initially shrinks the squares by a percentage in order to only grab the internal
         * color of each square, and not grab the outline itself. Default set to 1.5% for x and 1%
         * for y. */
        for(int i = 0; i < squares.size(); i++) {
            squares.get(i).x = (int)(squares.get(i).x + img.width() * PERC1);
            squares.get(i).y = (int)(squares.get(i).y + img.height() * PERC2);
            squares.get(i).height = (int)(squares.get(i).height - img.height() * (2*PERC2));
            squares.get(i).width = (int)(squares.get(i).width - img.width() * (2*PERC1));
        }
        /* Uses a mask to find the average color of a single square, then stores it in the List of
         * Scalars. */
        for(int i = 0; i < squares.size(); i++) {
            Mat mask = new Mat(img.height(), img.width(), 0);
            List<MatOfPoint> poly = new ArrayList<MatOfPoint>();
            Point[] points = new Point[4];

            points[0] = new Point(squares.get(i).x, squares.get(i).y);
            points[1] = new Point(squares.get(i).x + squares.get(i).width, squares.get(i).y);
            points[2] = new Point(squares.get(i).x + squares.get(i).width, squares.get(i).y + squares.get(i).height);
            points[3] = new Point(squares.get(i).x, squares.get(i).y + squares.get(i).height);
            poly.add(new MatOfPoint(points));

            fillPoly(mask, poly, new Scalar(255));

            colors.add(mean(img, mask));
        }
        return colors;
    }
}