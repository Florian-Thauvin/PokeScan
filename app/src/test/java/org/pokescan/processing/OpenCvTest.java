package org.pokescan.processing;

import android.graphics.Bitmap;
import com.asprise.ocr.Ocr;
import com.googlecode.tesseract.android.TessBaseAPI;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class OpenCvTest {
    private static  final String DIRECTORY = new File("src/test/java/org/pokescan/processing/dev").getAbsolutePath();

    private static final String RESULT_DIR = "/res/";

    private static final boolean FILE_LOG = false;

    private static final String JPG ="jpg";
    private static final String PNG ="png";

    private void loadLibs(){
        File openCv = new File("lib/opencv/x64/opencv_java3415.dll");
        System.out.println("Load OpenCV from " + openCv.getAbsolutePath());
        System.load(openCv.getAbsolutePath());
        System.out.println("OpenCV version" + Core.VERSION);


       /*File tesseract = new File("lib/tesseract/win32-x86-64/libtesseract3051.dll");
        System.load(tesseract.getAbsolutePath());*/
    }


    private void initResDir(){
        File dir = new File(DIRECTORY, RESULT_DIR);
        try {
        if(dir.exists()){
            System.out.println("Clean " + dir.getAbsolutePath());
            for(File file : dir.listFiles()){
                if(!file.delete()){
                    System.out.println("    - Can't delete file "+ file.getAbsolutePath());
                } else {
                    System.out.println("    - Delete file "+ file.getAbsolutePath());
                }
            }
        } else {
            Files.createDirectory(dir.toPath());
        }} catch (IOException e){
            System.out.println("Can't create rest directory to "+ dir.getAbsolutePath()+": "+e);
        }
    }

    @Test
    public void testProcessing() throws InterruptedException {
        loadLibs();

        initResDir();

        testProcessing("Evoli_Astres_119");
        testProcessing("Farfuret_Offensive_60");
        testProcessing("Meltan_Pokemon_45");
    }


    @Test
    public void testCollectionExtraction(){
        loadLibs();
        CardCollection collection = new CardCollection();
        Map<String, Mat> collectionToTest = new HashMap<>();

        File collections = new File(DIRECTORY, "/collection/");
        CardProcessing.initProcessing(collections);
        for(File file : collections.listFiles()) {
            String name = file.getName().replace(".png","");
            Mat card = loadMat(name, "/collection/", PNG);
            Imgproc.cvtColor(card, card, Imgproc.COLOR_BGR2GRAY);

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
    public void testOct(        ){

        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
        String s = ocr.recognize(new File[] {new File(getJpgFileName("Black", RESULT_DIR, PNG))}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        System.out.println("Result: " + s);
        // ocr more images here ...
        ocr.stopEngine();
    }

    private static  void testProcessing(String name){
        File dir = new File(DIRECTORY, String.format("%s/%s",RESULT_DIR, name));
        if(!dir.exists()){
            try {
                Files.createDirectory(dir.toPath());
                System.out.println("Create dir to "+ dir.toPath());
            } catch (IOException e){
                System.out.println("Can't create dir for " + dir.toPath());
            }
        }

        CardResult card = new CardResult(loadMat(name, null, JPG));

        CardProcessing.preProcess(card);
        CardProcessing.detectEdges(card);

    }

    private static Mat loadMat(String fileName, String path, String extension){
        String file =   getJpgFileName(fileName, path, extension);
        Mat mat= Imgcodecs.imread(file);

        if(FILE_LOG) {
            System.out.println("Mat load is " + (mat.empty() ? "" : "not") + " empty, from " + file);
        }
        return mat;
    }

    private  static void saveMat(String fileName,String dir, Mat mat){
        String file =   getJpgFileName(String.format("%s/%s",dir,fileName), RESULT_DIR, JPG);
        if(FILE_LOG){
        System.out.println("Mat saved is " + (mat.empty()? "":"not") +" empty, to " + file);}

        new Imgcodecs().imwrite(file, mat);
    }

    private static String getJpgFileName(String fileName, String subDir, String extension){
        return new File(String.format("%s%s%s.%s", DIRECTORY, subDir!=null ? subDir: "/",fileName,extension)).getAbsolutePath();
    }
}