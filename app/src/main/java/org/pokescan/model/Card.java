/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.model;

import android.media.Image;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    private final String name;

    /**
     * Id of the card in the collection
     */
    @NotNull
    @Pattern("[0-9]{3}")
    private final String id;

    /**
     * The collection
     */
    @NotNull
    private final Collection collection;

    // private Type type;

    /**
     * Image to display
     */
    private Image picture;

    public Card(@NotNull String name, @NotNull String id, String collection) {
        this.name = name;
        this.id = id;
        this.collection = new ArrayList();
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getCollection() {
        return "";
    }
}
