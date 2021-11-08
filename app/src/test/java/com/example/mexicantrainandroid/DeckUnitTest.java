package com.example.mexicantrainandroid;

import com.example.mexicantrainandroid.models.Tile;
import com.example.mexicantrainandroid.models.Deck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class DeckUnitTest {
    @Test
    public void retrieveDoubleCorrectly() {
        Deck newDeck = new Deck();

        Tile double_tile = newDeck.takeOutDoubleTiles(3);
        boolean double_ok = double_tile.is_double();
        assertTrue(double_ok);

        List<Tile> left_over_tiles = newDeck.takeOutBunchTiles(-1);
        assertEquals(left_over_tiles.size(), 54);
    }

    @Test
    public void retrieveManyTilesCorrectly() {
        Deck newDeck = new Deck();

        List<Tile> hand_tiles = newDeck.takeOutBunchTiles(10);
        assertEquals(hand_tiles.size(), 10);


        List<Tile> left_over_tiles = newDeck.takeOutBunchTiles(-1);
        assertEquals(left_over_tiles.size(), 45);
    }

}
