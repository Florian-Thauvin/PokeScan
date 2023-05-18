/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.ui.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.pokescan.common.logger.EMarker;
import org.pokescan.common.logger.PokeLogger;
import org.pokescan.common.logger.PokeLoggerFactory;

import java.io.IOException;

/**
 * Class used to manage the preview in real time of the camera
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    /**
     * The surface used to hold the preview
     */
    private final SurfaceHolder mHolder;
    /**
     * Camera device
     */
    private final Camera mCamera;
    /**
     * LOGGER
     */
    private static final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.START_AND_STOP, CameraLayout.class);

    /**
     * Class used to manage the camera preview on a surface view
     *
     * @param context
     *         global context
     * @param camera
     *         used camera
     */
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            LOGGER.info("Can't start the camera preview: ", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        holder.removeCallback(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }


        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            LOGGER.error("Can't start camera preview ", e);
        }
    }

}
