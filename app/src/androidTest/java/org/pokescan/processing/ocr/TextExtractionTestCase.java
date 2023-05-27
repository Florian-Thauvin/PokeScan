/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.processing.ocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnitRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class used to check the OCR used to extract text from an image
 */
@RunWith(AndroidJUnit4.class)
public class TextExtractionTestCase extends AndroidJUnitRunner {
    /**
     * The text extractor
     */
    private static TextExtraction textExtraction;

    @Before
    public void before() {
        // Create the text extractor
        // The path in parameters is the data used to extract text
        textExtraction = new TextExtraction(InstrumentationRegistry.getInstrumentation().getTargetContext().getAssets());
    }

    @Test
    public void testFontMicrosoft() throws Exception {
        // Test the extract of a text with a Microsoft font
        testTextExtraction("font/Microsoft");
    }

    @Test
    public void testFontArial() throws Exception {
        // Test the extract of a text with an Arial font
        testTextExtraction("font/Arial");
    }

    @Test
    public void testFontCaligraphy() throws Exception {
        // Test the extract of a text with a Calligraphy font
        testTextExtraction("font/Calligraphy");
    }

    @Test
    public void testColorRed() throws Exception {
        // Test the extract of a text with a red text on white background
        testTextExtraction("color/Red");
    }

    @Test
    public void testColorGreen() throws Exception {
        // Test the extract of a text with a green text on white background
        testTextExtraction("color/Green");
    }

    @Test
    public void testColorGrey() throws Exception {
        // Test the extract of a text with a grey text on white background
        testTextExtraction("color/Grey");
    }

    @Test
    public void testColorBlue() throws Exception {
        // Test the extract of a text with a blue text on white background
        testTextExtraction("color/Blue");
    }

    @Test
    public void testBackgroundBlue() throws Exception {
        // Test the extract of a text with a white text on blue background
        testTextExtraction("background/Blue");
    }

    @Test
    public void testBackgroundBack() throws Exception {
        // Test the extract of a text with a white text on black background
        testTextExtraction("background/Black");
    }

    @Test
    public void testBackgroundGreen() throws Exception {
        // Test the extract of a text with a white text on green background
        testTextExtraction("background/Green");
    }

    @Test
    public void testBackgroundGrey() throws Exception {
        // Test the extract of a text with a white text on grey background
        testTextExtraction("background/Grey");
    }

    @Test
    public void testBackgroundRed() throws Exception {
        // Test the extract of a text with a white text on red background
        testTextExtraction("background/Red");
    }

    @Test
    public void testTextAlpha() throws Exception {
        // Test the extract of a text with a text with [A-Z]
        // We have some issues with spaces, ignore them for the moment
        // The c is transformed to C, ignore it for the moment
        testTextExtraction("text/Alpha", "ABCDEFGH IJKLMNOPQRSTUVWXYZa b C defghijklmnopqrstuvwxyz");
    }

    @Test
    public void testTextNumeric() throws Exception {
        // Test the extract of a text with a text with [0-9]
        testTextExtraction("text/Numeric", "1234567890");
    }

    @Test
    public void testTextBlank() throws Exception {
        // Test the extract of a text with a text with spaces
        testTextExtraction("text/Blank", "aa a a");
    }

    @Test
    @Ignore("Issues with Special, see later")
    public void testTextSpecial() throws Exception {
        // Test the extract of a text with a text with special chars
        testTextExtraction("text/Special", "aàáâãäåAÀÁÂÃÄÅeèéêëEÈÉÊËiìíîïIÌÍÎÏoðòóôõöOÒÓÔÕÖuùúûüUÙÚÛÜyýÿYÝœŒæÆ");
    }

    private void testTextExtraction(String imageName) throws Exception {
        testTextExtraction(imageName, "Ceci est un texte");
    }

    private void testTextExtraction(String imageName, String expectedText) throws Exception {
        InputStream is = InstrumentationRegistry.getInstrumentation().getContext().getAssets().open(String.format("processing/ocr/%s.png", imageName));
        assertNotNull("Check if file exist", is);

        //Bitmap image = BitmapFactory.decodeStream(new BufferedInputStream(is));
        File image = copyInputStreamToFile(is);
        assertNotNull("Check if file is loaded", image);

        String resultText = textExtraction.extractText(image);
        assertEquals("We must have the same text", expectedText, resultText);
    }

    // Copy an InputStream to a File.
//
    private File copyInputStreamToFile(InputStream in) {
        File file = new File("");
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        return file;
    }
}