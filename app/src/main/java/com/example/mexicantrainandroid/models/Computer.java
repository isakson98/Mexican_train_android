package com.example.mexicantrainandroid.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Computer extends the capabilities of a Player class, but it is more streamlined
 * to just do the most optimal step in each turn (either play highest tiles or draw)
 *
 */
public class Computer extends Player{


    public Computer(String name) {
        super(name);
    }

    /**
     * most important function of computer class -> activated each turn once to conduct all the necessary
     * steps to play.
     * @param boneyard
     * @param all_trains
     * @return possible_move -> either empty (if skipping a turn) or some tiles -> if it played any
     */
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


    /**
     * computer plays drawn tile by matching it to an eligible train
     * @param all_trains
     */
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
