/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.ui.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import org.pokescan.common.logger.EMarker;
import org.pokescan.common.logger.PokeLogger;
import org.pokescan.common.logger.PokeLoggerFactory;

/**
 * Class used to manage and create the camera layout
 */
public class CameraLayout {
    /**
     * The surface used to display camera in real time
     */
    private final FrameLayout surfacePreview;

    /**
     * The button used to capture the card
     */
    private final Button captureButton;

    /**
     * The camera device
     */
    private Camera camera;

    /**
     * The camera preview manager
     */
    private CameraPreview cameraPreview;
    /**
     * Logger
     */
    private static final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.START_AND_STOP, CameraLayout.class);

    /**
     * Manager of the camera layout
     *
     * @param surfacePreview
     *         surface used to display camera in real time
     * @param captureButton
     *         button used to capture the card
     * @param context
     *         the global context
     */
    public CameraLayout(FrameLayout surfacePreview, Button captureButton, Context context) {
        LOGGER.info("Start camera layout");
        this.surfacePreview = surfacePreview;
        this.captureButton = captureButton;

        createCameraLayout(context);
    }

    /**
     * Method used to create the Camera layout
     */
    private void createCameraLayout(Context context) {
        try {
            // TODO update to new camera

            // First open the camera (need to permission)
            camera = Camera.open();
            // Force the image format
            camera.getParameters().setPictureFormat(ImageFormat.NV21);
            // Create the surface preview
            createCameraPreview(context);
            // Add a capture button to add the card
            createCaptureButton();
        } catch (Exception e) {
            LOGGER.error("Can't access camera: ", e);
        }
    }

    /**
     * Method used to clean and destroy the Camera layout
     */
    public void destroyCameraLayout() {
        surfacePreview.removeView(cameraPreview);
    }

    /**
     * Method used to manage the camera preview on real time
     *
     * @param context
     *         the global context
     */
    private void createCameraPreview(Context context) {
        // Create our Preview view and set it as the content of our activity.
        cameraPreview = new CameraPreview(context, camera);
        surfacePreview.addView(cameraPreview);
        // Attach the preview callback (for processing)
        // TODO camera.setPreviewCallback(Processing::processCamera);
    }

    /**
     * Method used to manage the camera capture button
     */
    private void createCaptureButton() {
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        camera.takePicture(null, null, new CardCapture());
                    }
                }
        );
    }
}
