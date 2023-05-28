package org.pokescan.processing;

import android.graphics.Bitmap;
import android.os.FileUtils;
import com.asprise.ocr.Ocr;
import com.googlecode.tesseract.android.TessBaseAPI;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.pokescan.processing.CardCollection;
import org.pokescan.processing.CardProcessing;
import org.pokescan.processing.CardResult;
import org.pokescan.processing.ocr.TextExtraction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Test class used to develop processing
 */
@Ignore("Can only be passed manually")
public class ProcessingDev {
    /**
     * Determines if we need to log all files
     */
    private static final boolean FILE_LOG = false;

    @BeforeClass
    public static void loadLibs(){
        File openCv = new File("lib/opencv/x64/opencv_java3415.dll");
        System.out.println("Load OpenCV from " + openCv.getAbsolutePath());
        System.load(openCv.getAbsolutePath());
        System.out.println("OpenCV version" + Core.VERSION);

        TestUtils.cleanDirectory(new File(TestUtils.DEV_DIRECTORY, TestUtils.RESULT_DIR));
    }


    @Test
    public void testProcessing() throws InterruptedException {
        testProcessing("Evoli_Astres_119");
        testProcessing("Farfuret_Offensive_60");
        testProcessing("Meltan_Pokemon_45");
    }


    @Test
    public void testCollectionExtraction(){
        CardCollection collection = new CardCollection();
        Map<String, Mat> collectionToTest = new HashMap<>();

        File collections = new File(TestUtils.DEV_DIRECTORY, "/collection/");
        for(File file : Objects.requireNonNull(collections.listFiles())) {
            String name = TestUtils.getImageNameWithoutExtension(file);
            Mat card = loadMat(name, "/collection/", TestUtils.PNG_EXTENSION);
            Imgproc.cvtColor(card, card, Imgproc.COLOR_BGR2GRAY);

            collection.addNewCollection(name, card);

            Mat toTest = new Mat();
            card.copyTo(toTest);
            collectionToTest.put(name, toTest);
        }

        for(Map.Entry<String, Mat> toTest : collectionToTest.entrySet()) {
            CardResult result = new CardResult(null);
            result.setCollectionMat(toTest.getValue());
            collection.getBestCollection(result);

            System.out.println("Collection {" + toTest.getKey()
                    + "} has for best collection {" + result.getCollection()
                    + "}, get results: " + result.getCollectionStats());
        }
    }

    @Test
    public void testOct(){
        simulateTextExtraction(new File(getImageFilePath("Black", "/ocr/", TestUtils.PNG_EXTENSION)));
    }

    private static  void testProcessing(String name){
        File dir = new File(TestUtils.DEV_DIRECTORY, String.format("%s/%s",TestUtils.RESULT_DIR, name));
        if(!dir.exists()){
            try {
                Files.createDirectory(dir.toPath());
                System.out.println("Create dir to "+ dir.toPath());
            } catch (IOException e){
                System.out.println("Can't create dir for " + dir.toPath());
            }
        }

        CardResult card = new CardResult(loadMat(name, null, TestUtils.JPG_EXTENSION));

        CardProcessing.preProcess(card);
        CardProcessing.detectEdges(card);

        saveMat("Processed", name,card.getProcessedMat());
        saveMat("Number", name,card.getNumberMat());
        saveMat("Name", name,card.getNameMat());
        saveMat("Collection", name, card.getCollectionMat());
    }

    private static Mat loadMat(String fileName, String path, String extension){
        String file =   getImageFilePath(fileName, path, extension);
        Mat mat= Imgcodecs.imread(file);

        if(FILE_LOG) {
            System.out.println("Mat load is " + (mat.empty() ? "" : "not") + " empty, from " + file);
        }
        return mat;
    }

    private  static void saveMat(String fileName,String dir, Mat mat){
        String file =   getImageFilePath(String.format("%s/%s",dir,fileName), TestUtils.RESULT_DIR, TestUtils.JPG_EXTENSION);
        if(FILE_LOG){
        System.out.println("Mat saved is " + (mat.empty()? "":"not") +" empty, to " + file);}

        Imgcodecs.imwrite(file, mat);
    }

    /**
     * Method used to get an image file name
     * @param fileName the file name
     * @param subDir the sub dir
     * @param extension the image extension
     * @return the file path
     */
    private static String getImageFilePath(String fileName, String subDir, String extension){
        return new File(String.format("%s%s%s%s", TestUtils.DEV_DIRECTORY, subDir!=null ? subDir: "/",fileName,extension)).getAbsolutePath();
    }

    private static String simulateTextExtraction(File file){
        Ocr.setUp();
        Ocr ocr = new Ocr();
        ocr.startEngine("eng", Ocr.SPEED_FASTEST);
        ocr.recognize(new File[] {file},  Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        ocr.stopEngine();
    }
}