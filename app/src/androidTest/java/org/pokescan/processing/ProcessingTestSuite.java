/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.processing;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.runner.RunWith;
import org.pokescan.model.Card;

@RunWith(AndroidJUnit4.class)
public class ProcessingTestSuite {
    /*@BeforeClass
    public static void beforeAll() {
        File openCV = new File("./libs/opencv/x64/opencv_java453.dll");
        Logger.info(EMarker.OPEN_CV, "Start openCV from", openCV.getAbsolutePath().toString());
        System.load(openCV.getAbsolutePath().toString());
    }*/

    private static void testImageProcessing(String file, Card expectedResult) {
        // TODO
        /*Mat matrix = new Imgcodecs().imread(file);
        Card result = Processing.extractInformations(matrix);

        assertEquals("Must have same name", expectedResult.getName(), result.getName());
        assertEquals("Must have same collection", expectedResult.getCollection(), result.getCollection());
        assertEquals("Must have same id", expectedResult.getId(), result.getId());*/
    }
}
