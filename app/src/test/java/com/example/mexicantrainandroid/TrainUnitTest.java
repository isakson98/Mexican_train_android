
package com.example.mexicantrainandroid;

import com.example.mexicantrainandroid.models.Tile;
import com.example.mexicantrainandroid.models.Train;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TrainUnitTest {

    @Test
    public void adds_new_tiles_Correctly() {

        Tile starting_tile = new Tile(5,5);
        Train new_train = new Train("Mexican", starting_tile);
        assertEquals(new_train.getTile_top_half(), 5);

        Tile next_tile = new Tile(6,5);
        new_train.add_new_tile(next_tile);
        assertEquals(new_train.getTile_top_half(), 6);

        Tile another_next_tile = new Tile(6,12);
        new_train.add_new_tile(another_next_tile);
        assertEquals(new_train.getTile_top_half(), 12);

    }


    @Test
    public void hand_tile_matches_endCorrectly() {

        // note that mexican does not have any tiles after constructor function ends
        // still functions
        Tile starting_tile = new Tile(5,5);
        Train new_train = new Train("Mexican", starting_tile);

        // comparing same denomination double
        Tile new_tile = new Tile(5,6);
        boolean tiles_match = new_train.hand_tile_matches_end(new_tile);
        assertTrue(tiles_match);

        // comparing same denomination double
        Tile newer_tile = new Tile(6,5);
        tiles_match = new_train.hand_tile_matches_end(newer_tile);
        assertTrue(tiles_match);

        // should not pass because none of the tiles match
        Tile another_next_tile = new Tile(6,12);
        tiles_match = new_train.hand_tile_matches_end(another_next_tile);
        assertFalse(tiles_match);
    }

}
