package com.example.mexicantrainandroid.models;


/*

Due to the unfortunate nature of Java not having a pair object,
I created this data structure to store combo move to be placed on the board game.
It only stores the name of the train that the tile should go on and the tile itself.

 */
public class ComboPair {

    public ComboPair(String train_name, Tile tile) {
        this.train_name = train_name;
        this.tile = tile;
    }
    public String train_name;
    public Tile tile;
}
