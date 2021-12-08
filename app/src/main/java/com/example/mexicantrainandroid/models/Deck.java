package com.example.mexicantrainandroid.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Deck class is responsible for dealing with the initial distribution of tiles
 * at the start of each round. It is capable of creating a new deck, taking out
 * the engine, distributing tiles to all players, and finally giving the rest of tiles
 * as the bone yard.
 *
 */

public class Deck implements Serializable {

    private final int double_n_set_size = 9;
    private List<Tile> entireDeck = new ArrayList<Tile>();

    /**
     * upon creating a Deck object, it creates a vector and shuffle it
     */
    public Deck() {
        this.populateDeck();
        this.shuffleDeck();
    }


    /**
     * shuffling array list of tiles to randomize result in the distribution
     */
    public void shuffleDeck(){
        Random rand = new Random(System.currentTimeMillis());
        Collections.shuffle(this.entireDeck, rand);
    }


    /**
     * find the start engine tile and pops it
     * @param doubleDenom -> calculated by based on round we are in at the time
     * @return double_tile -> tile taken out
     */
    public Tile takeOutDoubleTiles(int doubleDenom) {
        Tile double_tile = new Tile(doubleDenom, doubleDenom);

        for (int i = 0; i <= this.entireDeck.size(); i++) {
            if (this.entireDeck.get(i).equals(double_tile)) {
                this.entireDeck.remove(i);
                break;
            }
        }
        return double_tile;
    }

    /**
     * extract several tiles -> useful when distributing tiles at the start of the round
     * @param n_tiles -> either positive  number, or negative (take out all of them)
     * @return retrievedTiles -> list of random tiles take out
     */
    public List<Tile> takeOutBunchTiles(int n_tiles) {
        // verify there's no funny business with empty deck going on
        if (this.entireDeck.size() == 0) {System.exit(0);}

        // take all tiles left in the deck
        if (n_tiles == -1){
            n_tiles = this.entireDeck.size();
        }

        List retrievedTiles = new ArrayList<> (this.entireDeck.subList(0, n_tiles));
        this.entireDeck = this.entireDeck.subList(n_tiles, this.entireDeck.size());
        return retrievedTiles;
    }


    /**
     * initializing a vector of tiles to match the full deck
     */
    private void populateDeck() {
        for (int first_num = 0; first_num <= this.double_n_set_size; first_num++) {
            for (int second_num = first_num; second_num <= this.double_n_set_size; second_num++) {
                this.entireDeck.add(new Tile(first_num, second_num));
            }
        }
    }

}
