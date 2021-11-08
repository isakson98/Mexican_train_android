package com.example.mexicantrainandroid;

//package com.example.mexicantrainandroid.models;

import com.example.mexicantrainandroid.models.Tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TileUnitTest {


    @Test
    public void rotateCorrect() {
        Tile one = new Tile(1,2);
        one.rotate_tile();
        assertEquals(one.getLeft(), 2);
        assertEquals(one.getRight(), 1);
    }

    @Test
    public void isDoubleCorrect(){
        Tile one = new Tile(1,2);
        assertFalse(one.is_double());
        Tile two = new Tile(2,2);
        assertTrue(two.is_double());
    }

    @Test
    public void equalsCorrect(){
        Tile one = new Tile(1,2);
        Tile two = new Tile(2,2);

        boolean same_thing = one.equals(two);
        assertFalse(same_thing);

        Tile three = new Tile(2,1);
        same_thing = one.equals(three);
        assertTrue(same_thing);
    }



}