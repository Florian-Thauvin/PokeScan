/*
 * Copyright (c) PokeScan2023.
 */

package org.pokescan.processing.ocr;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import com.asprise.ocr.Ocr;
import com.googlecode.tesseract.android.TessBaseAPI;
import org.pokescan.common.logger.EMarker;
import org.pokescan.common.logger.PokeLogger;
import org.pokescan.common.logger.PokeLoggerFactory;
import org.pokescan.common.utils.storage.StorageUtils;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Class used to extract a text from a picture
 */
public class TextExtraction {
    // CF. https://github.com/ashomokdev/Tess-two_example

    /**
     * The path to the Tesseract lib
     */
    private static final String DATA_PATH = Paths.get(Environment.getExternalStorageDirectory().toString(), "PokeScan").toString();

    /**
     * Name used by Tesseract for the data
     */
    private static final String TESS_DATA_FOLDER_NAME = "tessdata";
    /**
     * The Tesseract API
     */
    private TessBaseAPI tessBaseApi;

    private final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.START_AND_STOP, TextExtraction.class);

    /**
     * Manager used to extract a text from a picture
     *
     * @param assets
     *         the AssetManager used to create directory for Tesseract and add all data files
     */
    public TextExtraction(AssetManager assets) {
        LOGGER.info("Start Tesseract with libs on ", DATA_PATH);

        // First create all directory and copy data
        prepareTesseract(assets);
        initTesseract();
    }

    /**
     * Method used to prepare the Tesseract directory and add all data files
     *
     * @param assets
     *         the AssetManager used to create directory for Tesseract and add all data files
     */
    private void prepareTesseract(AssetManager assets) {
        // First create the directory if needed
        boolean isTesseractFolderExisting = StorageUtils.prepareDirectory(DATA_PATH + TESS_DATA_FOLDER_NAME);

        if (isTesseractFolderExisting) {
            try {
                // Then copy all needed files
                copyTessDataFiles(TESS_DATA_FOLDER_NAME, assets);
            } catch (IOException e) {
                LOGGER.error("Can't add all Tesseract data files: ", e);
            }
        }
        // No else, the Tesseract app won't be created
    }

    /**
     * Method used to init the Tesseract API
     */
    private void initTesseract() {
        try {
            // Create the API
            tessBaseApi = new TessBaseAPI();
            tessBaseApi.init(DATA_PATH, "eng");
        } catch (Exception e) {
            LOGGER.error("Can't start Tesseract API: ", e);
            if (tessBaseApi == null) {
                LOGGER.error("TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }
    }


    /**
     * Copy Tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path
     *         - name of directory with .traineddata files
     */
    private void copyTessDataFiles(String path, AssetManager assets) throws IOException {
        // Get the list of files
        String[] fileList = assets.list(path);

        // Copy each file
        for (String fileName : fileList) {

            // Open file within the assets folder
            //If it is not already there copy it to the sdcard
            String dataFile = Paths.get(path, fileName).toString();
            String pathToDataFile = Paths.get(DATA_PATH, dataFile).toString();
            if (!(new File(pathToDataFile)).exists()) {
                // Create the streams
                InputStream in = assets.open(dataFile);
                OutputStream out = new FileOutputStream(pathToDataFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                // Close all streams
                in.close();
                out.close();

                LOGGER.info("Copy ", fileName, " data");
            }
        }
    }

    /**
     * Method used to extract the text from a picture
     *
     * @param bitmap
     *         the image to process
     * @return extracted text
     */
    public String extractText(File image) {
        /*tessBaseApi.setImage(bitmap);
        String extractedText = tessBaseApi.getUTF8Text();
        tessBaseApi.end();

        return extractedText;*/

        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
        String s = ocr.recognize(new File[]{image}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        System.out.println("Result: " + s);
        // ocr more images here ...
        ocr.stopEngine();
        return s;
    }
}
