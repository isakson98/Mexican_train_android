package com.example.mexicantrainandroid;

import com.example.mexicantrainandroid.models.Player;
import com.example.mexicantrainandroid.models.Tile;
import com.example.mexicantrainandroid.models.Deck;
import com.example.mexicantrainandroid.models.Train;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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


        Player one = new Player();
        one.deduce_eligible_trains(all_trains);


    }

}
