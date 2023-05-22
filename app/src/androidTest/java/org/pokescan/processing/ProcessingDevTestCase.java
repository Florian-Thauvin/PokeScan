package org.pokescan.processing;

import android.content.res.AssetManager;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.BeforeClass;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class ProcessingDevTestCase {

    private static final  String DIRECTORY ="processing/dev";

    private static final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.IMAGE_PROCESSING, ProcessingDevTestCase.class);

    @BeforeClass
    public static void beforeAll() {
        CardProcessing.loadOpenCv();
    }

    @Test
    public void testProcessing() throws Exception {
       AssetManager assets = InstrumentationRegistry.getInstrumentation().getContext().getAssets();

        InputStream is  = assets.open(getPath("Evoli_Astres_119"));

        int nRead;
        byte[] data = new byte[16 * 1024];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        byte[] bytes = buffer.toByteArray();
        Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_UNCHANGED);


        LOGGER.info("Loaded image is empty: ", mat.empty());

        Mat greyMat = new Mat();
        Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_BGR2GRAY);

        new Imgcodecs().imwrite(getPath("Grey"), greyMat);

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
        }
    }

    private String getPath(String fileName){
        String path = String.format("%s/%s.jpg", DIRECTORY, fileName);

        LOGGER.info("Load image from path: ", path);

        return path;
    }
}
