package com.example.mexicantrainandroid.models;

import java.util.ArrayList;
import java.util.List;


/*
Train class is responsible for containing all information about a train.
It's a 1 object Train per 1 Train relationship.
Each train can have a unique name and there's a slight distinction in how
a constructor handles whether a game is loaded or not
 */
public class Train {


    /* *********************************************************************
    Function Name: Train
    Purpose: this constructor is used when loading from file
    Parameters:
       const string& train_name
    Return Value: none
    Help received: none
    ********************************************************************* */
    public Train(String train_name, Tile starting_tile) {
        this.tile_top_half = Integer.MAX_VALUE;
        this.marker = false;
        this.ends_with_orphan_double = false;
        this.name = train_name;
        this.add_new_tile(starting_tile);
        if (train_name.equals("Mexican")) {
            this.train_tiles.clear();
        }
    }


    /* *********************************************************************
    Function Name: add_new_tile
    Purpose: adds new tile to train tile stack. verification takes elsewhere
    Parameters: new_tile coming from a player
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void add_new_tile(Tile new_tile) {
        // this tile is a starting tile, so order does not matter
        if (this.tile_top_half == Integer.MAX_VALUE) {
            this.tile_top_half = new_tile.getLeft();
        }
        // comparing to which end does the new tile match and
        // setting the other end of incoming tile as new top half tile
        else if (this.tile_top_half == new_tile.getRight()) {
            this.tile_top_half = new_tile.getLeft();
        }
        else if (this.tile_top_half == new_tile.getLeft()) {
            this.tile_top_half = new_tile.getRight();
            new_tile.rotate_tile();
        }
        // finally, push that new tile into the train stack
        this.train_tiles.add(new_tile);
    }


    /*
    Function Name: hand_tile_matches_end
    Purpose: confirms if tile that you want to insert matches the end of the train
    Parameters: none
    Return Value: existing boolean
    Help received: none
     */
    public boolean hand_tile_matches_end(Tile player_tile) {
        return this.tile_top_half == player_tile.getRight() || this.tile_top_half == player_tile.getLeft();
    }


    /*
    GETTERS AND SETTERS + PRIVATE VARIABLES
     */


    /* *********************************************************************
    Function Name: getTile_top_half
    Purpose: gets current last number of the train
    Parameters: none
    Return Value: nothing
    Help received: none
    ********************************************************************* */
    public int getTile_top_half() {
        return tile_top_half;
    }


    /* *********************************************************************
    Function Name: setTile_top_half
    Purpose: sets new top half of a new tile
    Parameters: tile_top_half
    Return Value: nothing
    Help received: none
    ********************************************************************* */
    public void setTile_top_half(int tile_top_half) {
        this.tile_top_half = tile_top_half;
    }


    /* *********************************************************************
    Function Name: isMarker
    Purpose: checks marker
    Parameters: none
    Return Value: none
    Help received: none
    ********************************************************************* */
    public boolean isMarker() {
        return marker;
    }


    /* *********************************************************************
    Function Name: setMarker
    Purpose: sets marker
    Parameters: marker
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void setMarker(boolean marker) {
        this.marker = marker;
    }


    /* *********************************************************************
    Function Name: isEnds_with_orphan_double
    Purpose: returns true if orphan double is at the end of this train instance
    Parameters: none
    Return Value: none
    Help received: none
    ********************************************************************* */
    public boolean isEnds_with_orphan_double() {
        return ends_with_orphan_double;
    }


    /* *********************************************************************
    Function Name: setEnds_with_orphan_double
    Purpose: sets orphan double
    Parameters: ends_with_orphan_double
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void setEnds_with_orphan_double(boolean ends_with_orphan_double) {
        this.ends_with_orphan_double = ends_with_orphan_double;
    }


    /* *********************************************************************
    Function Name: isCurrent_eligible_train
    Purpose: returns true if current train instance is eligible
    Parameters: eligible boolean
    Return Value:  none
    Help received: none
    ********************************************************************* */
    public boolean isCurrent_eligible_train() {
        return current_eligible_train;
    }


    /* *********************************************************************
    Function Name: setCurrent_eligible_train
    Purpose: train sets whether it is eligible for current player
    Parameters: current_eligible_train
    Return Value:  none
    Help received: none
    ********************************************************************* */
    public void setCurrent_eligible_train(boolean current_eligible_train) {
        this.current_eligible_train = current_eligible_train;
    }


    /* *********************************************************************
    Function Name: getTrain_tiles
    Purpose: train gets whether it is eligible for current player
    Parameters: none
    Return Value: existing boolean
    Help received: none
    ********************************************************************* */
    public List<Tile> getTrain_tiles() {
        return train_tiles;
    }


    /* *********************************************************************
    Function Name: set_whole_new_train
    Purpose: after loading a perfect, proper train, there's no need to go through
        the hassle of adding tiles one by one. dangerous function, so must use it
        with caution
    Parameters: vector<Tile> loaded_train
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void setTrain_tiles(List<Tile> train_tiles) {
        this.setTile_top_half(train_tiles.get(train_tiles.size()-1).getRight());
        this.train_tiles = train_tiles;
    }

    private final String name;
    private int tile_top_half;
    private boolean marker;
    private boolean ends_with_orphan_double;
    private boolean current_eligible_train;

    List<Tile> train_tiles = new ArrayList<>();

}
