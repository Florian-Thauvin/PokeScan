/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.common.utils.storage;

import org.pokescan.common.logger.EMarker;
import org.pokescan.common.logger.PokeLogger;
import org.pokescan.common.logger.PokeLoggerFactory;

import java.io.File;

/**
 * Class used to manage the Android storage
 */
public class StorageUtils {

    /**
     * The logger
     */
    private static final PokeLogger LOGGER = PokeLoggerFactory.getLogger(EMarker.START_AND_STOP, StorageUtils.class);


    /**
     * Constructor
     */
    private StorageUtils(){
        // Hidden
    }

    /**
     * Method used to create a directory at the
     *
     * @param path
     *         path to the directory
     * @return if the directory is created or exist
     */
    public static boolean prepareDirectory(String path) {
        boolean isExisting = false;
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    LOGGER.error("Creation of directory ", path," failed, check does Android Manifest have permission to write to external storage");
                } else {
                    LOGGER.info("Directory created at: ", path);
                    isExisting = true;
                }
            } else {
                LOGGER.info("Directory already existing at: ", path);
                isExisting = true;
            }
        } catch (Exception e) {
            LOGGER.error("Can't create folder to ", path,", unexpected exception: ", e);
        }
        return isExisting;
    }
}
