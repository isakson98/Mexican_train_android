package com.example.mexicantrainandroid.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/*

Deck class is responsible for dealing with the initial distribution of tiles
at the start of each round. It is capable of creating a new deck, taking out
the engine, distributing tiles to all players, and finally giving the rest of tiles
as the bone yard.
 */

public class Deck {


    /* *********************************************************************
    Function Name: Deck constructor
    Purpose: upon creating a Deck object, it creates a vector and shuffle it
    Parameters: none
    Return Value: none
    Help received: none
    ********************************************************************* */
    public Deck() {
        this.populateDeck();
        this.shuffleDeck();
    }


    /* *********************************************************************
    Function Name: shuffleDeck
    Purpose: shuffling array list of tiles to randomize result in the distribution
    Parameters: none
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void shuffleDeck(){
        Random rand = new Random(System.currentTimeMillis());
        Collections.shuffle(this.entireDeck, rand);
    }


    /* *********************************************************************
    Function Name: takeOutDoubleTiles
    Purpose: find the start engine tile and pops it
    Parameters:
        doubleDenom -> a double tile number
    Return Value: Tile
    Help received: none
    ********************************************************************* */
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


    /* *********************************************************************
    Function Name: takeOutBunchTiles
    Purpose: extract tiles
    Parameters:
        n_tiles -> specified number of tiles for each player
                           if value is -1, extract everything
    Return Value:
        player_hand -> vector of tiles for a player to start game with
    Help received: none
    ********************************************************************* */
    public List<Tile> takeOutBunchTiles(int n_tiles) {
        // verify there's no funny business with empty deck going on
        if (this.entireDeck.size() == 0) {System.exit(0);}

        // take all tiles left in the deck
        if (n_tiles == -1){
            n_tiles = this.entireDeck.size();
        }

        List retrievedTiles = this.entireDeck.subList(0, n_tiles);
        this.entireDeck = this.entireDeck.subList(n_tiles, this.entireDeck.size());
        return retrievedTiles;
    }


    /* *********************************************************************
    Function Name: populateDeck
    Purpose: initializing a vector of tiles to match the full deck
    Parameters: none
    Return Value: none
    Help received: none
    ********************************************************************* */
    private void populateDeck() {
        for (int first_num = 0; first_num <= this.double_n_set_size; first_num++) {
            for (int second_num = first_num; second_num <= this.double_n_set_size; second_num++) {
                this.entireDeck.add(new Tile(first_num, second_num));
            }
        }
    }

    private final int double_n_set_size = 9;
    private List<Tile> entireDeck = new ArrayList<Tile>();

}
