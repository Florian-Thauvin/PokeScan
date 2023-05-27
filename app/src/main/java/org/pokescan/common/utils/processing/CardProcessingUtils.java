package org.pokescan.common.utils.processing;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class CardProcessingUtils {
    public static MatOfPoint findMaxContour(List<MatOfPoint> contourList) {
        double maxVal = 0;
        MatOfPoint max = contourList.get(0);
        for (int contourIdx = 1; contourIdx < contourList.size(); contourIdx++)
        {
            MatOfPoint contour = contourList.get(contourIdx);
            double contourArea = Imgproc.contourArea(contour);
            if (maxVal < contourArea)            {
                maxVal = contourArea;
                max = contour;
            }
        }
        return max;
    }

    public static Mat createBinaryKernel(int size) {
        return Imgproc.getStructuringElement(Imgproc.THRESH_BINARY, new Size((size * size) + 1, (size * size) + 1));
    }

    public static Mat cropImage(Mat rawMat, Rect rect){
        return new Mat(rawMat, rect);
    }
}
