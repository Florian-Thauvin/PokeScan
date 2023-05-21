/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.common.logger;

/**
 * Class used to instantiate a logger
 */
public class PokeLoggerFactory {

    /**
     * Constructor
     */
    private PokeLoggerFactory(){
        // Hidden
    }

    /**
     * Method used to get a new logger instance
     * @param marker type of logger
     * @param caller class who call the logger
     * @return a new Logger instance
     */
    public static PokeLogger getLogger(EMarker marker, Class<?> caller) {
        return new PokeLogger(marker, caller.getSimpleName());
    }
}
