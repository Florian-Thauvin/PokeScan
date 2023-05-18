package org.pokescan.processing.ocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TextExtractionTestSuite extends AndroidJUnitRunner {
    private static TextExtraction textExtraction;

    @Before
    public void before() {
        textExtraction = new TextExtraction(InstrumentationRegistry.getInstrumentation().getTargetContext().getAssets());
    }

    @Test
    public void testMicrosoft() throws Exception {
        testTextExtraction("Microsoft");
    }

    private void testTextExtraction(String imageName) throws Exception {
        testTextExtraction(imageName, "Ceci est un texte");
    }

    private void testTextExtraction(String imageName, String expectedText) throws Exception {
        InputStream is = InstrumentationRegistry.getInstrumentation().getContext().getAssets().open(String.format("processing/ocr/%s.png", imageName));
        assertNotNull("Check if file exist", is);

        Bitmap image = BitmapFactory.decodeStream(new BufferedInputStream(is));
        assertNotNull("Check if file is loaded", image);

        String resultText = textExtraction.extractText(image);
        assertEquals("We must have the same text", expectedText, resultText);
    }
}