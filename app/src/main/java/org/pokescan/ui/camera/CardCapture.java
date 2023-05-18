/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.ui.camera;

import android.hardware.Camera;

/**
 * Class used to manage the capture of a card
 */
public class CardCapture implements Camera.PictureCallback {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        // TODO
/*
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            // Log.d(TAG, "Error creating media file, check storage permissions");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            // Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            //  Log.d(TAG, "Error accessing file: " + e.getMessage());
        }*/
    }
}