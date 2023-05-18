/*
 * Copyright (c) PokeScan2023.
 */

package org.pokescan.processing;

import android.hardware.Camera;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.pokescan.common.logger.EMarker;
import org.pokescan.common.logger.PokeLogger;
import org.pokescan.common.logger.PokeLoggerFactory;
import org.pokescan.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to process a card to extract all informations
 */
public class CardProcessing {

    private static final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.START_AND_STOP, CardProcessing.class);

    /**
     * Code used to init the Card processing
     */
    static {
        // We need to init OpenCV lib first
        if (!OpenCVLoader.initDebug()) {
            LOGGER.error("Unable to load OpenCV");
        } else {
            LOGGER.info("OpenCV loaded Successfully");
        }
    }

    // TODO

    public static void processCamera(byte[] data,
                                     Camera camera) {
        Mat rawImage = new Mat(camera.getParameters().getPictureSize().width, camera.getParameters().getPictureSize().height, CvType.CV_8UC3);
        rawImage.put(0, 0, data);

        Mat processed = new Mat(camera.getParameters().getPictureSize().width, camera.getParameters().getPictureSize().height, CvType.CV_8UC3);

        Imgproc.cvtColor(rawImage, processed, Imgproc.COLOR_RGB2GRAY);

        //camera.addCallbackBuffer(FormatProcessing.bytesFromMat(processed));
    }

    public static Card extractInformations(Mat mat) {
        // CF https://stackoverflow.com/questions/62116719/read-text-on-card
        Mat greyMat = new Mat();
        Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_BGR2GRAY);

        Mat processedMat = new Mat();
        Imgproc.threshold(greyMat, processedMat, 150, 255, Imgproc.THRESH_BINARY);

        Mat edgesMat = new Mat();
        Imgproc.Canny(processedMat, edgesMat, 30, 200);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edgesMat, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contours.size() > 0) {
            Mat maxContour = new Mat();
            //Core.max(contours);
            Rect rect = Imgproc.boundingRect(maxContour);


            Mat cropedFrame = new Mat(rect.height, rect.width, mat.type());
            Imgproc.resize(mat, cropedFrame, cropedFrame.size(), Imgproc.INTER_AREA);
        }


        return new Card();
    }
}
