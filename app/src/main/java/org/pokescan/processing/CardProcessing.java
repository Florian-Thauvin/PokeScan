/*
 * Copyright (c) PokeScan2023.
 */

package org.pokescan.processing;

import android.content.res.AssetManager;
import android.hardware.Camera;
import org.jetbrains.annotations.NotNull;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.pokescan.common.logger.EMarker;
import org.pokescan.common.logger.PokeLogger;
import org.pokescan.common.logger.PokeLoggerFactory;
import org.pokescan.common.utils.processing.CardProcessingUtils;
import org.pokescan.common.utils.processing.ImageDrawerUtils;
import org.pokescan.model.Card;

import java.io.File;
import java.util.*;

/**
 * Class used to process a card to extract all informations
 */
public class CardProcessing {

    private static final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.START_AND_STOP, CardProcessing.class);

    private  static  final Mat KERNEL = CardProcessingUtils.createBinaryKernel(5);

    private static CardCollection collection;

    public static final void initProcessing(File collectionDirectory){
        if (!OpenCVLoader.initDebug()) {
            LOGGER.error("Unable to load OpenCV");
        } else {
            LOGGER.info("OpenCV loaded Successfully");
        }

        collection = new CardCollection();
        if(collectionDirectory != null) {
            initCollections(collectionDirectory);
        }
    }

    private static void initCollections(File collectionDirectory) {
        for(File file : collectionDirectory.listFiles()) {
            String name = file.getName().replace(".png","");
            Mat card = Imgcodecs.imread(file.getAbsolutePath());
            Imgproc.cvtColor(card, card, Imgproc.COLOR_BGR2GRAY);
        }
    }

    @SuppressWarnings("unused")
    public static CardResult extractCardInformations(Mat rawImage){
        CardResult result = new CardResult(rawImage);

        preProcess(result);
        detectEdges(result);
        collection.getBestCollection(result);

        return result;
    }

    protected static void preProcess(CardResult card){
        // First pass mat to Gray scale
        Mat proccessedMat = new Mat();
        Imgproc.cvtColor(card.getRawMat(), proccessedMat, Imgproc.COLOR_BGR2GRAY);

        //Mat blurMat = new Mat();
        //Imgproc.blur(greyMat, blurMat, new Size(3, 3));
        //saveMat("Blur", name, blurMat);

        // Then, apply a threshold to discrete image
        Imgproc.threshold(proccessedMat, proccessedMat, 130, 255, Imgproc.THRESH_BINARY);

        card.setProcessedMat( proccessedMat);
    }

    protected static void detectEdges(CardResult card){
        Mat processedMat = card.getProcessedMat();

        Mat erodeMat = new Mat();
        Imgproc.erode(processedMat, erodeMat, KERNEL);

        // Get contours
        List<MatOfPoint> contourList = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(erodeMat, contourList, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_NONE);

        MatOfPoint max = CardProcessingUtils.findMaxContour(contourList);

        Rect contour= Imgproc.boundingRect(max);

        int percent = 9;
        int height = contour.height / (percent+1);
        int width = contour.width;

        Rect topCard = new Rect(contour.x, contour.y, width/ 2, height);
        Rect bottom = new Rect(contour.x, contour.y + height * percent, width, height);

        if(card.isOnDebug()) {
            Mat contourMat = new Mat();
            card.getRawMat().copyTo(contourMat);
            ImageDrawerUtils.drawImageEdges(contourMat, contourList, hierarchy, contour, topCard, bottom);
            card.addDebugImage("Contour", contourMat);
        }


        card.setNameMat(CardProcessingUtils.cropImage(card.getProcessedMat(), topCard));

        /*
        Mat pokemonNumberAndCollection = new Mat(processedMat, bottom);

        Rect collectionRect = extractCollection(pokemonNumberAndCollection);
        Rect numberRect =*/
        card.setNumberMat(CardProcessingUtils.cropImage(card.getProcessedMat(), bottom));
        card.setNumberMat(CardProcessingUtils.cropImage(card.getProcessedMat(), bottom));
    }
}
