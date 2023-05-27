package org.pokescan.common.utils.processing;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class ImageDrawerUtils {
    public static final int LIGHT_THICK = 3;

    public static final int BOLD_THICK = 50;

    public static final Scalar GREEN_COLOR =  new Scalar(0, 255, 0);
    public static final Scalar RED_COLOR =  new Scalar(255, 0, 0);

    public static void drawImageEdges(Mat mat, List<MatOfPoint> contourList ,  Mat hierarchy, Rect... contours){
        Mat contourMat = new Mat();
        Imgproc.cvtColor(mat, contourMat, Imgproc.COLOR_GRAY2BGR);

        Scalar color = new Scalar(0, 0, 255);
        Imgproc.drawContours(contourMat, contourList, -1, color, 3, Imgproc.LINE_8,
                hierarchy, 2, new Point());

        for(Rect contour: contours) {
            ImageDrawerUtils.drawRect(contourMat, contour, ImageDrawerUtils.BOLD_THICK);
        }
    }

    public  static void drawRect(Mat mat, Rect rect, int thick){
        Imgproc.rectangle(mat,new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), GREEN_COLOR,thick );
    }
}
