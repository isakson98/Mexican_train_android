package com.example.mexicantrainandroid.models;

import androidx.annotation.Nullable;

public class Tile {

    private int right;

    public int getRight() {
        return right;
    }

    private int left;

    public int getLeft() {
        return left;
    }

    /* *********************************************************************
    Function Name: Tile
    Purpose: constructor with two parameters
    Parameters:
        int left
        int right
    Algorithm: none
    Return Value: none
    Help received: https://www.geeksforgeeks.org/overriding-equals-method-in-java/
    ********************************************************************* */
    public Tile(int left, int right) {
        this.left = left;
        this.right = right;
    }

//    @Override
    /* *********************************************************************
    Function Name: equals
    Purpose: compares two tiles (orverloading it)
    Parameters: none
    Algorithm:
       - compares left side of one tile with left and right side of the other tile and vice versa
    Return Value: true or false
    Help received: https://www.geeksforgeeks.org/overriding-equals-method-in-java/
    ********************************************************************* */
    public boolean equals(Tile other_tile) {
        // If the object is compared with itself then return true
        if (other_tile == this) {
            return true;
        }
        if (this.left == other_tile.left && this.right == other_tile.right ) {
            return true;
        }
        if (this.left == other_tile.right && this.right == other_tile.left ) {
            return true;
        }
        return false;
    }

    /* *********************************************************************
    Function Name: rotate_tile
    Purpose: swaps the pair values for organizational purposes of tiles in the train
    Parameters: none
    Algorithm :
       - usual swap operation
       - used to insert into the train correctly
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void rotate_tile() {
        int temp = this.left;
        this.left = this.right;
        this.right = temp;
    }


    /* *********************************************************************
    Function Name: is_double
    Purpose: verifies of the tile is a double
    Parameters: none
    Return Value: true or false
    Help received: none
    ********************************************************************* */
    public boolean is_double() {
        if (this.left == this.right) {return true;}
        else { return false; }
    }

}
