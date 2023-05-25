package org.pokescan.processing.dev;

import android.graphics.Bitmap;
import com.googlecode.tesseract.android.TessBaseAPI;
import org.junit.Test;
import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class OpenCvTest {
    private static  final String DIRECTORY = new File("src/test/java/org/pokescan/processing/dev").getAbsolutePath();

    private static final String RESULT_DIR = "/res/";

    private static final boolean FILE_LOG = false;

    private static final String JPG ="jpg";
    private static final String PNG ="png";

    @Test
    public void test() throws InterruptedException {
        loadLibs();

        initResDir();

        testProcessing("Evoli_Astres_119");
        testProcessing("Farfuret_Offensive_60");
        testProcessing("Meltan_Pokemon_45");
    }

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

        Mat rawMat =loadMat(name, null, JPG);

        Mat preprocessedMat = preProcessImage(rawMat, name);
        detectEdges(preprocessedMat, name);
    }

    private static Mat preProcessImage(Mat rawMat, String name){
        // First pass mat to Gray scale
        Mat greyMat = new Mat();
        Imgproc.cvtColor(rawMat, greyMat, Imgproc.COLOR_BGR2GRAY);

        //saveMat("Grey", name, greyMat);

        Mat blurMat = new Mat();
        Imgproc.blur(greyMat, blurMat, new Size(3, 3));
        //saveMat("Blur", name, blurMat);

        // Then, apply a threshold to discrete image
        Mat threshMat = new Mat();
        Imgproc.threshold(blurMat, threshMat, 130, 255, Imgproc.THRESH_BINARY);

        //saveMat("Thresh",name, threshMat);


        return threshMat;
    }

    private static void detectEdges(Mat mat, String name){
      /*  Mat blurMat = new Mat();
        Imgproc.blur(mat, blurMat, new Size(5, 5));
        saveMat("Blur2", name,blurMat);*/

        // We apply a erode
        Mat erodeMat = new Mat();
        // Prepare the kernel for erosion
        int size = 5;
        Mat kernel =  Imgproc.getStructuringElement(Imgproc.THRESH_BINARY, new Size((size*size) + 1, (size*size)+1));
        Imgproc.erode(mat, erodeMat, kernel);

        //saveMat("Eroded", name, erodeMat);

        // Get contours
        List<MatOfPoint> contourList = new ArrayList<>();
        Mat hierarchey = new Mat();
        Imgproc.findContours(erodeMat, contourList, hierarchey, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_NONE);

        System.out.println("Number of contours " + contourList.size());

        // Tmp code
        Mat contourMat = new Mat();
        Imgproc.cvtColor(mat, contourMat, Imgproc.COLOR_GRAY2BGR);

        Scalar color = new Scalar(0, 0, 255);
        Imgproc.drawContours(contourMat, contourList, -1, color, 3, Imgproc.LINE_8,
                hierarchey, 2, new Point() ) ;

        MatOfPoint max = findMaxContour(contourList);
        MatOfPoint2f contour_ = new MatOfPoint2f();
        max.convertTo(contour_, CvType.CV_32FC2);
        RotatedRect contour = Imgproc.minAreaRect(contour_);
        //Rect contour= Imgproc.boundingRect(max);

        int percent = 9;
        int height = contour.height / (percent+1);
        int width = contour.width ;
        Rect topCard = new Rect(contour.x, contour.y, width/ 2, height);
        Rect bottom = new Rect(contour.x, contour.y + height * percent, width, height);

        int thick2 = 50;
        drawRect(contourMat, contour,thick2);
        drawRect(contourMat, topCard,thick2);
        drawRect(contourMat, bottom,thick2);

        saveMat("Contour",name, contourMat);

        Mat pokemonName = new Mat(mat, topCard);
        saveMat("Name", name, pokemonName);

        Mat pokemonNumberAndCollection = new Mat(mat, bottom);
        saveMat("NumberAndCollection", name, pokemonNumberAndCollection);

        Mat pokemonColl = extractCollection(pokemonNumberAndCollection);
        saveMat("Collection", name, pokemonColl);

       checkCollection(pokemonNumberAndCollection, name, "Astres");
        checkCollection(pokemonNumberAndCollection, name, "Origine");
        checkCollection(pokemonNumberAndCollection, name, "Tempete");
    }

     private static Mat extractCollection(Mat mat){
            Mat res = new Mat();
            mat.copyTo(res);

            Mat dilateMat = new Mat();
            mat.copyTo(dilateMat);
         Imgproc.blur(dilateMat, dilateMat, new Size(4, 4));

           /* int size = 7;
            Mat kernel =  Imgproc.getStructuringElement(Imgproc.THRESH_BINARY, new Size((size*size) + 1, (size*size)+1));
            Imgproc.erode(dilateMat, dilateMat,kernel);*/
             int size2 = 4;
             Mat kernel2 =  Imgproc.getStructuringElement(Imgproc.THRESH_BINARY, new Size((size2*size2) + 1, (size2*size2)+1));
            Imgproc.dilate(dilateMat, dilateMat,kernel2);


             List<MatOfPoint> contourList = new ArrayList<>();
             Mat hierarchey = new Mat();
              Imgproc.findContours(dilateMat, contourList, hierarchey, Imgproc.RETR_TREE,
                 Imgproc.CHAIN_APPROX_NONE);

            List<Mat> possible = new ArrayList<>();

         Imgproc.cvtColor(res, res, Imgproc.COLOR_GRAY2BGR);
            for(MatOfPoint points : contourList){
                drawRect(res, Imgproc.boundingRect(points),3);
                //possible.add(new Mat(mat, Imgproc.boundingRect(points)));
            }

            // TODO how ?
            return  res;//possible.get(0);
     }

    private static MatOfPoint findMaxContour(List<MatOfPoint> contourList) {
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

    private static void checkCollection(Mat image, String name, String collection) {
        Mat template = loadMat(collection, "/collection/", PNG);
        Imgproc.cvtColor(template, template, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(template, template, 130, 255, Imgproc.THRESH_BINARY);
        int size = image.height() / 4;
        Imgproc.resize(template, template, new Size(size,size));

        Mat detected = new Mat();
        image.copyTo(detected);

        int result_cols = detected.cols() - template.cols() + 1;
        int result_rows = detected.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(detected, template, result, Imgproc.TM_CCOEFF);
        Core.normalize(result, result, 0, 100, Core.NORM_MINMAX, -1, new Mat());

        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        // / Show me what you got
        Imgproc.cvtColor(detected, detected, Imgproc.COLOR_GRAY2BGR);

        System.out.println("Get a mmr for "+ collection + " with min at "+ mmr.minVal + " and a max to " + mmr.maxVal);

        if(mmr.maxVal > 0.8) {
            Imgproc.rectangle(detected, mmr.maxLoc, new Point(mmr.maxLoc.x + template.cols(),
                    mmr.maxLoc.y + template.rows()), new Scalar(0, 0, 255), 3);
        }
        if(mmr.minVal > 0.8) {
            Imgproc.rectangle(detected, mmr.minLoc, new Point(mmr.minLoc.x + template.cols(),
                    mmr.minLoc.y + template.rows()), new Scalar(0, 255, 0), 3);
        }


        saveMat(String.format("Res_%s", collection), name, detected);
    }


    private void getText(Mat mat){
        File tessData = new File("lib/tesseract/tessdata");
        TessBaseAPI tessBaseApi = new TessBaseAPI();
        tessBaseApi.init(tessData.getAbsolutePath(), "eng");

        Bitmap bmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bmp);
        tessBaseApi.setImage(bmp);
        String extractedText = tessBaseApi.getUTF8Text();
        tessBaseApi.end();

        System.out.println(extractedText);
    }

    private  static void drawRect(Mat mat, Rect rect, int thick){
        Scalar color2 = new Scalar(0, 255, 0);
        Imgproc.rectangle(mat,new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), color2,thick );
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