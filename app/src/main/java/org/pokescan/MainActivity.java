/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.pokescan.common.logger.EMarker;
import org.pokescan.common.logger.PokeLogger;
import org.pokescan.common.logger.PokeLoggerFactory;
import org.pokescan.common.utils.conversion.ConvertMatUtils;
import org.pokescan.common.utils.permissions.RequestPermissionsTool;
import org.pokescan.processing.CardCollection;
import org.pokescan.processing.CardProcessing;
import org.pokescan.ui.camera.CameraLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Main Class of the application, launched at startup
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Tool used to request and manage permissions
     */
    private RequestPermissionsTool requestTool;

    /**
     * Manager for the camera layout
     */
    private CameraLayout cameraLayout;

    private static final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.START_AND_STOP, MainActivity.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // First call super
        super.onCreate(savedInstanceState);
        // Create the layer
        setContentView(R.layout.activity_main);

        LOGGER.info("Application started");

        // Request all needed permission if not already granted and if the android API allows it
        requestPermissions();
        // Create the camera layout with the Surface display and capture button
        cameraLayout = new CameraLayout(findViewById(R.id.camera_preview), findViewById(R.id.button_capture), this);

        //CardProcessing.initProcessing(new File(this.getClass().getResource("/collections").getFile()));
        try (InputStream is = getAssets().open("collections")){
            CardProcessing.initProcessing(ConvertMatUtils.copyInputStreamToFile(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if all needed permissions has been granted
        boolean grantedAllPermissions = true;
        // Check each result
        for (int grantResult : grantResults) {
            // If at least one result is negative, we will call the request callback
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                grantedAllPermissions = false;
                break;
            }
        }

        if (grantResults.length != permissions.length || (!grantedAllPermissions)) {
            // At least one permission is not granted, need to inform user
            requestTool.onPermissionDenied();
        } else {
            LOGGER.info("All permissions granted");
            // All the permissions has been granted !
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LOGGER.info("Application ended");
        // Destroy all elements
        cameraLayout.destroyCameraLayout();
        cameraLayout = null;
        requestTool = null;
    }

    /**
     * Function used to request all permissions
     */
    private void requestPermissions() {
        // First, we need to check if the SDK has this feature
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Get the list of permission needed:
            // - WRITE_EXTERNAL_STORAGE is used to manage the resources for the OCR Tesseract
            // - CAMERA is used to access camera for preview
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            LOGGER.info("Request permissions: ", permissions);
            // Create the request tool
            requestTool = new RequestPermissionsTool();
            // Request all needed permissions
            requestTool.requestPermissions(this, permissions);
        }
    }
}