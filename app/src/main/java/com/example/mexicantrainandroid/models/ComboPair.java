package com.example.mexicantrainandroid.models;


import java.io.Serializable;

/**
 * Due to the unfortunate nature of Java not having a pair object,
 * I created this data structure to store combo move to be placed on the board game.
 * It only stores the name of the train that the tile should go on and the tile itself.
 *
 * this class does not need setters because the train name and tile need to be set only
 * once in the constructor
 */
public class ComboPair implements Serializable {

    private String train_name;
    private Tile tile;

    /**
     * @param train_name
     * @param tile
     */
    public ComboPair(String train_name, Tile tile) {
        this.train_name = train_name;
        this.tile = tile;
    }

    /**
     * getter for train_name
     * @return train_name
     */
    public String getTrain_name() {
        return train_name;
    }

    /**
     * getter for tile
     * @return tile
     */
    public Tile getTile() {
        return tile;
    }


}
