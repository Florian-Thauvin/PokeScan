/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.model;

import android.media.Image;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Definition of a Card
 */
public class Card implements AbstractData {
    // TODO

    /**
     * Name of the Pokemon
     */
    @NotNull
    private String name;

    /**
     * Id of the card in the collection
     */
    @NotNull
    @Pattern("[0-9]{3}")
    private String id;

    /**
     * The collection
     */
    @NotNull
    private Collection collection;

    // private Type type;

    /**
     * Image to display
     */
    private Image picture;
}
