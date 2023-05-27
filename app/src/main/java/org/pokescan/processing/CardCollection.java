package org.pokescan.processing;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.pokescan.common.utils.processing.ImageDrawerUtils;
import org.pokescan.model.Card;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CardCollection {
    private Map<String, Mat> collectionMappedByName;

    public CardCollection(){
        this.collectionMappedByName = new HashMap<>();
    }

    public void addNewCollection(String name, Mat template){
        collectionMappedByName.put(name, template);
    }

    public void getBestCollection(CardResult card){
        for(Map.Entry<String, Mat> entry : collectionMappedByName.entrySet()){
            checkCollection(card, entry);
        }

        String bestCollection=  Collections.max(card.getCollectionStats().entrySet(), Map.Entry.comparingByValue()).getKey();
        card.setCollection(bestCollection);
    }

    protected static void checkCollection(CardResult card, Map.Entry<String, Mat> collection) {
        int kernelSize = 2;
        Mat kernel =  Imgproc.getStructuringElement(Imgproc.THRESH_BINARY, new Size((kernelSize*kernelSize) + 1, (kernelSize*kernelSize)+1));

        Mat template = new Mat();
        collection.getValue().copyTo(template);
        //Imgproc.threshold(template, template, 130, 255, Imgproc.THRESH_BINARY);
        int size = card.getCollectionMat().height() / 4;
        if(template.size().height < size && template.size().width < size) {
            //Imgproc.resize(template, template, new Size(size, size));
        }
       /* Imgproc.dilate(template, template, kernel);
        Imgproc.erode(template, template, kernel);*/

        Mat detected = new Mat();
        card.getCollectionMat().copyTo(detected);


        //Imgproc.dilate(detected, detected, kernel);
        // Imgproc.erode(detected, detected, kernel);

        Mat result = new Mat();
        Imgproc.matchTemplate(detected, template, result, Imgproc.TM_CCOEFF_NORMED);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        if(card.isOnDebug() && mmr.maxVal > 0.8) {
            Imgproc.rectangle(detected, mmr.maxLoc, new Point(mmr.maxLoc.x + template.cols(),
                    mmr.maxLoc.y + template.rows()), ImageDrawerUtils.RED_COLOR, ImageDrawerUtils.LIGHT_THICK);
            card.addDebugImage(collection.getKey(), detected);
        }

        card.getCollectionStats().put(collection.getKey(), mmr.maxVal);
    }


}
