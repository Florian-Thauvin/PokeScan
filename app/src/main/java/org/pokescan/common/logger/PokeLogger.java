/*
 * Copyright (c) PokeScan2023.
 */

package org.pokescan.common.logger;

import android.util.Log;

import java.text.DateFormat;
import java.util.*;

/**
 * Custom logger
 */
public class PokeLogger {

    private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.FRANCE);

    /**
     * The actual Marker
     */
    private final EMarker marker;
    /**
     * The context calling
     */
    private final String context;

    /**
     * Constructor for factory
     *
     * @param marker
     *         actual Marker
     * @param context
     *         context calling
     */
    protected PokeLogger(EMarker marker, String context) {
        this.marker = marker;
        this.context = context;
    }

    /**
     * Log an info message
     *
     * @param message
     *         the message to log
     * @param infoList
     *         complementary info
     * @param <T>
     *         any type
     */
    @SafeVarargs
    public final <T> void info(String message, T... infoList) {
        logMessage(Log.INFO, message, infoList);
    }

    /**
     * Log a warn message
     *
     * @param message
     *         the message to log, can be with %s
     * @param infoList
     *         complementary info
     * @param <T>
     *         any type
     */
    @SafeVarargs
    public final <T> void warn(String message, T... infoList) {
        logMessage(Log.WARN, message, infoList);
    }

    /**
     * Log an error message
     *
     * @param message
     *         the message to log, can be with %s
     * @param infoList
     *         complementary info
     * @param <T>
     *         any type
     */
    @SafeVarargs
    public final <T> void error(String message, T... infoList) {
        logMessage(Log.ERROR, message, infoList);
    }

    /**
     * Log a message
     *
     * @param level
     *         the log level
     * @param message
     *         the message to log, can be with %s
     * @param infoList
     *         complementary info
     * @param <T>
     *         any type
     */
    @SafeVarargs
    private final <T> void logMessage(int level, String message, T... infoList) {
        // Create a list of info
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(message);

        // Add all info as string
        for (T info : infoList) {
            if (info instanceof Exception) {
                joiner.add(((Exception) info).getMessage());
            } else {
                joiner.add(info != null ? info.toString() : "null");
            }
        }

        // Get the actual date
        String date = dateFormat.format(new Date());
        // Create the full message
        String fullMessage = String.format("[%s] - [%s]: %s", date, context, joiner.toString());
        // Print the message
        Log.println(level, marker.toString(), fullMessage);
    }
}
