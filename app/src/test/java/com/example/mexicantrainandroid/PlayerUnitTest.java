package com.example.mexicantrainandroid;

import com.example.mexicantrainandroid.models.ComboPair;
import com.example.mexicantrainandroid.models.Player;
import com.example.mexicantrainandroid.models.Tile;
import com.example.mexicantrainandroid.models.Deck;
import com.example.mexicantrainandroid.models.Train;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerUnitTest {
    @Test
    public void retrieveDoubleCorrectly() {
        Train mexican = new Train("Mexican",  new Tile(0,0));
        Train other_train = new Train("Human", new Tile(0,0));

        Map<String, Train> all_trains = new HashMap<String, Train>();

        all_trains.put("Mexican", mexican);
        all_trains.put("Human", other_train);


        Player one = new Player("Human");
        one.deduce_eligible_trains(all_trains);
    }

    @Test
    public void drawingBoneyardCorrectly() {

        // setting up trains
        Train one_train = new Train("Mexican", new Tile(4,4));
        Train second_train = new Train("Human", new Tile(5,5));
        Map<String, Train> all_trains = new HashMap<String, Train>();
        all_trains.put(one_train.getName(), one_train);
        all_trains.put(second_train.getName(), second_train);

        // setting up players deck and boneyard
        Deck deck = new Deck();
        Player one = new Player("Human");
        one.setHand(deck.takeOutBunchTiles(15));
        List <Tile> bones = deck.takeOutBunchTiles(-1);


        int bone_before_extraction = bones.size();
        int hand_before_extraction = one.getHand().size();
        one.draw_from_boneyard(bones, all_trains);

        // reduced by one
        assertEquals(bones.size()+1, bone_before_extraction);
        // increased by one
        assertEquals(one.getHand().size()-1, hand_before_extraction);

    }


    @Test
    public void verifyTilesCorrectlyOneCombo(){

        // setting up trains
        Train one_train = new Train("Mexican", new Tile(4,4));
        Train second_train = new Train("Human", new Tile(5,5));
        Map<String, Train> all_trains = new HashMap<String, Train>();
        all_trains.put(one_train.getName(), one_train);
        all_trains.put(second_train.getName(), second_train);


        one_train.setCurrent_eligible_train(true);
        second_train.setCurrent_eligible_train(true);


        // setting up players deck and boneyard
        Deck deck = new Deck();
        Player one = new Player("Human");
        one.setHand(deck.takeOutBunchTiles(15));
        List <Tile> bones = deck.takeOutBunchTiles(-1);


        List<ComboPair> list_combos = new ArrayList<>();
        list_combos.add(new ComboPair("Mexican", new Tile(5,5)));

        boolean itll_work = one.verify_tiles(list_combos,all_trains);
        // placing one double with many tiles left is not allowed
        assertFalse(itll_work);


        list_combos = new ArrayList<>();
        list_combos.add(new ComboPair("Mexican", new Tile(4,5)));

        itll_work = one.verify_tiles(list_combos,all_trains);
        // placing one double with many tiles left is not allowed
        assertTrue(itll_work);

    }





}
