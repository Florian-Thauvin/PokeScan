package main.java;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private static  final String DIRECTORY = "src/main/java/";

    private static final String RESULT_DIR = "res/";

    /**
     * Test class used for processing
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("OpenCV " + Core.VERSION);

        File dir = new File(DIRECTORY, RESULT_DIR);
        if(dir.exists()){
            System.out.println("Clean " + dir.getAbsolutePath());
            for(File file : dir.listFiles()){
                if(!file.delete()){
                    System.out.println("    - Can't delete file "+ file.getAbsolutePath());
                } else {
                    System.out.println("    - Delete file "+ file.getAbsolutePath());
                }
            }
        }

        test("Evoli_Astres_119");
        test("Farfuret_Offensive_60");
    }

    private static  void test(String name){
        File dir = new File(DIRECTORY, String.format("%s/%s",RESULT_DIR, name));
        if(!dir.exists()){
            try {
                Files.createDirectory(dir.toPath());
            } catch (IOException e){
                System.out.println("Can't create dir for " + name);
            }
        }

        Mat rawMat =loadMat(name);

        Mat preprocessedMat = preProcessImage(rawMat, name);
        detectEdges(preprocessedMat, name);
    }

    private static Mat preProcessImage(Mat rawMat, String name){
        // First pass mat to Gray scale
        Mat greyMat = new Mat();
        Imgproc.cvtColor(rawMat, greyMat, Imgproc.COLOR_BGR2GRAY);

        saveMat("Grey", name, greyMat);

        Mat blurMat = new Mat();
        Imgproc.blur(greyMat, blurMat, new Size(3, 3));
        saveMat("Blur", name, blurMat);

        // Then, apply a threshold to discrete image
        Mat threshMat = new Mat();
        Imgproc.threshold(blurMat, threshMat, 130, 255, Imgproc.THRESH_BINARY);

        saveMat("Thresh",name, threshMat);


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

        saveMat("Eroded", name, erodeMat);



       /* Mat cannyMat = new Mat();
        Imgproc.Canny(erodeMat, cannyMat, 20,20,5, false);

        saveMat("Canny",name, cannyMat);
*/
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



       Rect contour= Imgproc.boundingRect(max);

       int percent = 9;
       int height = contour.height / (percent+1);
       int width = contour.width / 2;
        Rect topCard = new Rect(contour.x, contour.y, width, height);
        Rect bottom = new Rect(contour.x, contour.y + height*percent, width, height);

        drawRect(contourMat, contour);
        drawRect(contourMat, topCard);
        drawRect(contourMat, bottom);

        saveMat("Contour",name, contourMat);
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

    private  static void drawRect(Mat mat, Rect rect){
        Scalar color2 = new Scalar(0, 255, 0);
        int thick2 = 50;
        Imgproc.rectangle(mat,new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), color2,thick2 );
    }

    private static Mat loadMat(String fileName){
     String file =   getJpgFileName(fileName, false);
       Mat mat= Imgcodecs.imread(file);

        System.out.println("Mat load is " + (mat.empty()? "":"not") +" empty, from " + file);
        return mat;
    }

    private  static void saveMat(String fileName,String dir, Mat mat){
        String file =   getJpgFileName(String.format("%s/%s",dir,fileName), true);
        System.out.println("Mat saved is " + (mat.empty()? "":"not") +" empty, to " + file);

        new Imgcodecs().imwrite(file, mat);
    }

    private static String getJpgFileName(String fileName, boolean isResult){
        return new File(String.format("%s%s%s.jpg", DIRECTORY, isResult ? RESULT_DIR: "",fileName)).getAbsolutePath();
    }
}