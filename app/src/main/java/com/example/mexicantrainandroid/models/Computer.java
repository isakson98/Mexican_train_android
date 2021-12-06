package com.example.mexicantrainandroid.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Computer extends Player{


    public Computer(String name) {
        super(name);
    }

    /* *********************************************************************
    Function Name: play
    Purpose: allows the computer player to play
    Parameters:
        all_trains -> holds all trains, fully updated
    Return Value: true or false
     Help received: none
    ********************************************************************* */
    public List<ComboPair> play(List<Tile> boneyard, Map<String, Train> all_trains ) {

        // by default mark this player as not having played this turn
        this.player_cant_play = false;

        List<ComboPair> possible_move = this.ask_for_help(all_trains);

        // computer shall draw
        if (possible_move.size() == 0){
            //draw if successful and try playing it
            if (this.draw_from_boneyard(boneyard, all_trains)) {
                if(this.verify_drawn_tile_playable(all_trains)) {
                    this.computer_plays_drawn_tile(all_trains);
                }
            }
            // unsuccessful draw -> means boneyard empty -> means player didn't play this turn
            else {
                this.player_cant_play = true;
            }
        }
        // computer shall place tiles
        else {
            this.place_tiles(possible_move, all_trains);
        }

        return possible_move;
    }


    /* *********************************************************************
    Function Name: computer_plays_drawn_tile
    Purpose: allows the computer player to play
    Parameters:
        all_trains -> holds all trains, fully updated
    Return Value: none
     Help received: none
    ********************************************************************* */
    private void computer_plays_drawn_tile(Map<String, Train>  all_trains) {

        Tile drawn_tile = this.hand.get(this.hand.size()-1);

        for (Train train_only : all_trains.values()) {
            if (train_only.hand_tile_matches_end(drawn_tile)) {
                List<ComboPair> train_draw_tile = new ArrayList<>();
                ComboPair draw_tile_pair = new ComboPair(train_only.getName(), drawn_tile);
                this.place_tiles(train_draw_tile, all_trains);
            }
        }

    }

}
