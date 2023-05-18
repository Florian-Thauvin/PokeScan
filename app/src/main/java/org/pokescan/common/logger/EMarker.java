/*
 * Copyright (c) PokeScan2023.
 */

package org.pokescan.common.logger;

/**
 * List of all markers used in the application
 */
public enum EMarker {
    /**
     * Maker used to log the start or the stop of application
     */
    START_AND_STOP,
    /**
     * Maker used to log all image processing
     */
    IMAGE_PROCESSING,
    /**
     * Maker used to log all access to database
     */
    DATA_ACCESS,
    /**
     * Marker used to log all request for card
     */
    CARD_REQUEST
}
