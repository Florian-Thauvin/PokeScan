/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.common.logger;

public class PokeLoggerFactory {
    public static PokeLogger getLogger(EMarker marker, Class<?> caller) {
        return new PokeLogger(marker, caller.getSimpleName());
    }
}
