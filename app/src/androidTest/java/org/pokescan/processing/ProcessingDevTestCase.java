package org.pokescan.processing;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.pokescan.common.logger.EMarker;
import org.pokescan.common.logger.PokeLogger;
import org.pokescan.common.logger.PokeLoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class ProcessingDevTestCase {
      private static final  String DIRECTORY ="processing/dev";

    private static final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.IMAGE_PROCESSING, ProcessingDevTestCase.class);

    @BeforeClass
    public static void beforeAll() {
        CardProcessing.initProcessing(null);
    }

    @Test
    public void testProcessing() throws Exception {

        File dirpath = InstrumentationRegistry.getInstrumentation().getContext().getExternalFilesDir(null);
        File file = new File(dirpath, DIRECTORY);
        if(!file.exists()) {
            Files.createDirectory(file.toPath().toAbsolutePath());
        }

        Mat mat = readMatFromFile(getPath("Evoli_Astres_119"));

        LOGGER.info("Loaded image is empty: ", mat.empty());
        writeMatToFile(getPath("Grey"), mat);

       /* Mat greyMat = new Mat();
        Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_BGR2GRAY);

        writeMatToFile(getPath("Grey"), mat);
        // new Imgcodecs().imwrite(getPath("Grey"), greyMat);

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


            new Imgcodecs().imwrite(getPath("Croped"), cropedFrame);
        }*/
    }

    private Mat readMatFromFile(String file)throws  IOException{
        AssetManager assets = InstrumentationRegistry.getInstrumentation().getContext().getAssets();
        InputStream is  = assets.open(file);

        int nRead;
        byte[] data = new byte[16 * 1024];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        byte[] bytes = buffer.toByteArray();
        Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_UNCHANGED);

        return mat;
    }
        private void writeMatToFile( String path, Mat mat){
        byte[] buff = new byte[(int) (mat.total() *  mat.channels())];
        mat.get(0, 0, buff);

        File dirpath = InstrumentationRegistry.getInstrumentation().getContext().getExternalFilesDir(null);
        File file = new File(dirpath, path);
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                LOGGER.error("Can't create file ", file.getPath(), e);
            }
        }
        try ( FileOutputStream stream = new FileOutputStream(file)){
            stream.write(buff);
            LOGGER.info("File ", file.getPath(), " writed");
            stream.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String getPath(String fileName){
        String path = String.format("%s/%s.jpg", DIRECTORY, fileName);

        LOGGER.info("Load image from path: ", path);

        return path;
    }
}
