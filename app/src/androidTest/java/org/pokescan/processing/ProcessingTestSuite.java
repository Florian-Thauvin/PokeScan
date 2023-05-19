/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.processing;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.pokescan.model.Card;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProcessingTestSuite {
    /*@BeforeClass
    public static void beforeAll() {
        File openCV = new File("./libs/opencv/x64/opencv_java453.dll");
        Logger.info(EMarker.OPEN_CV, "Start openCV from", openCV.getAbsolutePath().toString());
        System.load(openCV.getAbsolutePath().toString());
    }*/

    @Test
    public void testBackgroundFantominus() throws CardProcessingException {
        testImageProcessing("background/Fantominus_Evolution_47", new Card("Fantominus", "47", "Evolutions"));
    }

    @Test
    public void testBackgroundOsselait() throws CardProcessingException {
        testImageProcessing("background/Osselait_Esprits_105", new Card("Osselait", "105", "Harmonie des Esprits"));
    }

    @Test
    public void testBackgroundZamazenta() throws CardProcessingException {
        testImageProcessing("background/Zamazenta_Epee_196", new Card("ZamazentaV", "196", "Epee et Bouclier"));
    }

    @Test
    public void testBackgroundRamoloss() throws CardProcessingException {
        testImageProcessing("background/Ramoloss_Alliance_42", new Card("Ramoloss", "42", "Alliance Infaillible"));
    }

    @Test
    public void testBackgroundFarfuret() throws CardProcessingException {
        testImageProcessing("background/Farfuret_Offensive_60", new Card("Farfuret", "60", "Offensive Vapeur"));
    }

    @Test
    public void testBackgroundLixy() throws CardProcessingException {
        testImageProcessing("background/Lixy_Destinees_43", new Card("Lixy", "43", "Destinées Futures"));
    }

    @Test
    public void testBackgroundGringolem() throws CardProcessingException {
        testImageProcessing("background/Gringolem_Tenebres_78", new Card("Gringolem", "078", "Ténèbres Embrasées"));
    }

    @Test
    public void testBackgroundFouinette() throws CardProcessingException {
        testImageProcessing("background/Fouinette_Tenebres_138", new Card("Fouinette", "138", "Ténèbres Embrasées"));
    }

    @Test
    public void testBackgroundMeltan() throws CardProcessingException {
        testImageProcessing("background/Meltan_Pokemon_45", new Card("Meltan", "045", "Pokémon GO"));
    }


    @Test(expected = CardProcessingException.class)
    public void testLanguageEnglish() throws CardProcessingException {
        extractCardInformation("language/English");
    }

    @Test(expected = CardProcessingException.class)
    public void testLanguageJapanese() throws CardProcessingException {
        extractCardInformation("language/Japanese");
    }

    @Test
    public void testLanguageFrench() throws CardProcessingException {
        testImageProcessing("language/French", new Card("Migalos", "H3", "Aquapolis"));
    }

    @Test
    public void testEvoliExplorateurs() throws CardProcessingException {
        testImageProcessing("same/Evoli_Explorateurs_83", new Card("Evoli", "83", "Explorateurs Obscurs"));
    }

    @Test
    public void testEvoliSoleil() throws CardProcessingException {
        testImageProcessing("same/Evoli_Soleil_101", new Card("Evoli", "101", "Soleil et Lune"));
    }

    @Test
    public void testEvoliGlaciation() throws CardProcessingException {
        testImageProcessing("same/Evoli_Glaciation_89", new Card("Evoli", "89", "Glaciation Plasma"));
    }

    @Test
    public void testEvoliAstres() throws CardProcessingException {
        testImageProcessing("same/Evoli_Astres_119", new Card("Evoli", "119", "Astres Radieux"));
    }

    @Test
    public void testQualityAngleBottomRight() throws CardProcessingException {
        testImageProcessing("quality/Angle_Bottom_Right", new Card("Evoli", "101", "Soleil et Lune"));
    }

    @Test
    public void testQualityAngleBottom() throws CardProcessingException {
        testImageProcessing("quality/Angle_Bottom", new Card("Evoli", "101", "Soleil et Lune"));
    }


    @Test
    public void testQualityAngleInverse() throws CardProcessingException {
        testImageProcessing("quality/Inverse", new Card("Evoli", "101", "Soleil et Lune"));
    }

    @Test
    public void testQualityQuality() throws CardProcessingException {
        testImageProcessing("quality/Quality", new Card("Evoli", "101", "Soleil et Lune"));
    }

    @Test
    public void testQualityOverlight() throws CardProcessingException {
        testImageProcessing("quality/Overlight", new Card("Evoli", "101", "Soleil et Lune"));
    }

    @Test
    public void testQualityAngleRight() throws CardProcessingException {
        testImageProcessing("quality/AngleRight", new Card("Evoli", "101", "Soleil et Lune"));
    }

    @Test
    public void testQualityUnzoomed() throws CardProcessingException {
        testImageProcessing("quality/Unzoomed", new Card("Evoli", "101", "Soleil et Lune"));
    }

    @Test
    public void testQualityVertical() throws CardProcessingException {
        testImageProcessing("quality/Vertical", new Card("Evoli", "101", "Soleil et Lune"));
    }

    @Test
    public void testSpecialArcheomire() throws CardProcessingException {
        testImageProcessing("special/Archeomire_Impact_60", new Card("Archéomire", "60", "Impact des Destins"));
    }

    @Test
    public void testSpecialFunecire() throws CardProcessingException {
        testImageProcessing("special/Funecire_Origine_24", new Card("Funécire", "024", "Origine Perdue"));
    }

    @Test
    public void testSpecialBrocelome() throws CardProcessingException {
        testImageProcessing("special/Brocelome_Origine_16", new Card("Brocélôme", "016", "Origine Perdue"));
    }

    @Test(expected = CardProcessingException.class)
    public void testGamesYuGiYo() throws CardProcessingException {
        extractCardInformation("games/YuGiYo");
    }

    @Test(expected = CardProcessingException.class)
    public void testGamesFeuFollet() throws CardProcessingException {
        extractCardInformation("games/FeuFollet");
    }

    @Test(expected = CardProcessingException.class)
    public void testGamesJackieChan() throws CardProcessingException {
        extractCardInformation("games/JackieChan");
    }

    @Test(expected = CardProcessingException.class)
    public void testGamesPrinceval() throws CardProcessingException {
        extractCardInformation("games/Princeval");
    }

    private static void testImageProcessing(String file, Card expectedResult) throws CardProcessingException {
        Card result = extractCardInformation(file);

        assertEquals("Must have same name", expectedResult.getName(), result.getName());
        assertEquals("Must have same collection", expectedResult.getCollection(), result.getCollection());
        assertEquals("Must have same id", expectedResult.getId(), result.getId());
    }

    private static Card extractCardInformation(String file) throws CardProcessingException {
        Mat matrix = new Imgcodecs().imread(file);
        return CardProcessing.extractInformations(matrix);
    }
}
