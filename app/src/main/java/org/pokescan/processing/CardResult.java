package org.pokescan.processing;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.pokescan.model.Card;

import java.util.HashMap;
import java.util.Map;

public class CardResult extends Card{
    private Mat rawMat;

    private Mat processedMat;

    private Mat collectionMat;

    private Mat nameMat;

    private Mat numberMat;

    private Map<String, Double> collectionStats;

    private Map<String, Mat> debugImages;

    private boolean isOnDebug = false;

    public CardResult(Mat rawMat){
        super("","","");
        this.rawMat = rawMat;
        this.collectionStats = new HashMap<>();
    }


    public Mat getRawMat() {
        return rawMat;
    }

    public Mat getProcessedMat() {
        return processedMat;
    }

    public void setProcessedMat(Mat processedMat) {
        this.processedMat = processedMat;
    }

    public Mat getCollectionMat() {
        return collectionMat;
    }

    public void setCollectionMat(Mat collectionMat) {
        this.collectionMat = collectionMat;
    }

    public Mat getNameMat() {
        return nameMat;
    }

    public void setNameMat(Mat nameMat) {
        this.nameMat = nameMat;
    }


    public Mat getNumberMat() {
        return numberMat;
    }

    public void setNumberMat(Mat numberMat) {
        this.numberMat = numberMat;
    }

    public boolean isOnDebug() {
        return isOnDebug;
    }

    public void enableDebugMode(){
        this.isOnDebug = true;
        this.debugImages = new HashMap<>();
    }

    public void addDebugImage(String name, Mat debugImage){
        if(!isOnDebug){
            throw new UnsupportedOperationException("We can only add debug image in debug mode");
        }
        debugImages.put(name, debugImage);
    }

    public Map<String, Double> getCollectionStats() {
        return collectionStats;
    }

    public void setCollection(String collection){
        this.collection = collection;
    }
}
