package com.example.mexicantrainandroid.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/*

This player class is universal: it has functionality that both Human players and
Computer players might need. It is capable of fully updating and monitoring
player's decisions -> verifies their legality within the scope of this game's rules,
and it is also capable of giving advice on what is the best move

 */
public class Player implements Serializable {


    public Player(String name) {
        this.name = name;
    }


    /* *********************************************************************
    Function Name: hand_is_empty
    Purpose: finds out if hand is empty (useful to see if empty hand -> game done)
    Parameters:none
    Return Value: integer
    Help received: none
    ********************************************************************* */
    public boolean hand_is_empty(){
        return this.hand.isEmpty();
    }


    /* *********************************************************************
    Function Name: add_hand_points
    Purpose: add up the points at the end of the round
    Parameters: none
    Return Value: none
    Help received: none
    ********************************************************************* */
    public int add_hand_points(){
        int sum = 0;
        for (Tile element : this.hand) {
            sum += element.getLeft() + element.getRight();
        }
        this.score += sum;
        return sum;
    }

    // Not sure if gonna need it, depends on GUI implementation
    // void print_hand();

    /* *********************************************************************
    Function Name: deduce_eligible_trains
    Purpose: determines eligible trains: two routes
    - trains that have orphan double
    - or personal, Mexican, and opponents' that have markers on them
    Parameters:
        all_trains -> passed by reference hash map of trains
    Algorithm:
       - reinitialize to be all trains ineligible first
       - return trains with orphans first
       - otherwise set this player's train, Mexican train, and other player's train(if marker present) eligible
    Return Value: none -> updated made in all_trains, hash map that is used by reference
    Help received: none
    ********************************************************************* */
    public void deduce_eligible_trains(Map<String, Train> all_trains) {
        // reinitialize to have all trains to be ineligible at the start of a player's turn
        for (Train single : all_trains.values()) {
            single.setCurrent_eligible_train(false);
        }

        boolean orphans_present = false;
        /*
         * first find orphan double trains, can't play other trains until you get these sorted
         */
        for (Train single : all_trains.values()) {
            if (single.isEnds_with_orphan_double()) {
                orphans_present = true;
                single.setCurrent_eligible_train(true);
            }
        }
        // main priority is orphan trains, can't play other trains
        if (orphans_present)
            return;

        /*
         * if no orphan trains, take personal train, mexican train, or opponent's that has a marker
         */

        all_trains.get("Mexican").setCurrent_eligible_train(true);
        all_trains.get(this.name).setCurrent_eligible_train(true);

        // adding opponent's trains that have markers
        // marker simply means that other players can also play
        for (Train single : all_trains.values()) {
            // add a train only if it has a marker AND it's not this player's train
            if (single.isMarker())
                single.setCurrent_eligible_train(true);
        }

    }
    /* *********************************************************************
    Function Name: draw_from_boneyard
    Purpose: draws a tile from the boneyard, adds it to the players hand
    Parameters:
        bone_yard -> vector of tiles from the boneyard, passed by reference
    Return Value:
        bool -> contributes to whether to add marker to personal train or not
    Help received: none
    ********************************************************************* */
    public boolean draw_from_boneyard(List<Tile> bone_yard, Map<String, Train> all_trains) {
        if (bone_yard.isEmpty()) {
            all_trains.get(this.name).setMarker(true);
            this.setPlayer_cant_play(true);
            return false;
        }

        Tile tile_drawn = bone_yard.get(bone_yard.size()-1);

        for (Iterator<Tile> iterator = bone_yard.iterator(); iterator.hasNext();) {
            Tile iterating_tile = iterator.next();
            if (tile_drawn == iterating_tile) {
                iterator.remove();
                break;
            }
        }
        this.hand.add(tile_drawn);

        return true;

    }

    /* *********************************************************************
    Function Name: verify_drawn_tile_playable
    Purpose: verifies the one tile drawn from the boneyard is eligible
    Parameters:
        all_trains -> all trains in the round up-to-date
    Algorithm:
       - get the last tile in the vector
       - iterate across all trains to see if at least one of them matches a tile
       - return immediately if one matches
       - once out of the loop, we know that the drawn tile is unplayable at thde moment
    Return Value:
        bool -> true if player_cant_play, false otherwise
    Help received: none
    ********************************************************************* */
    public boolean verify_drawn_tile_playable(Map<String, Train> all_trains) {

        Tile drawn_tile = this.hand.get(this.hand.size()-1);

        boolean drawn_tile_can_be_played = false;

        for (Train single : all_trains.values()) {
            List<ComboPair> drawn_tile_single_combo = new ArrayList<ComboPair>();
            ComboPair single_combo = new ComboPair(single.getName(), drawn_tile);
            drawn_tile_single_combo.add(single_combo);

            drawn_tile_can_be_played = this.verify_tiles(drawn_tile_single_combo, all_trains);

            if (drawn_tile_can_be_played)
                return true;
        }

        // at this point we know this player misses a turn, so his train needs to be marked
        all_trains.get(this.name).setMarker(true);
        return false;

    }


    /* *********************************************************************
    Function Name: verify_tiles
    Purpose: verifies selected tiles: makes sure the moves are not illegal
    Parameters:
        moves_requested -> vector of <train name, tile to be placed> pairs
    Algorithm:
       - verifies tiles firstly depending on the size of the move (number of tiles)
          - mainly order of tiles, and if they are doubles and order of doubles
       - verifies if the tail of the train matches at least one side of the tile you want to place
    Return Value: true or false
    Help received: none
    ********************************************************************* */

    // TEMP
    public boolean verify_tiles(List<ComboPair> moves_requested, Map<String, Train> all_trains){
        // exit if any of the trains selected are not eligible
        for (ComboPair single_pair : moves_requested) {
            if (! all_trains.get(single_pair.train_name).isCurrent_eligible_train())
                return false;
        }

        /*
         * verifying the order of the tiles for all 3 cases
         */
        if (moves_requested.size() == 1) {
            if (moves_requested.get(0).tile.is_double() && this.hand.size() > 1 )
                return false;
        }
        else if (moves_requested.size() == 2) {
            if (!moves_requested.get(0).tile.is_double())
                return false;
            else if (!moves_requested.get(1).tile.is_double())
                return false;
            // can only place second as double if these doubles are all you have left (if you have 2 tiles to place)
            else if (this.hand.size() != 2)
                return false;
        }
        else if (moves_requested.size() == 3) {
            boolean first_double = moves_requested.get(0).tile.is_double();
            boolean second_double = moves_requested.get(1).tile.is_double();
            boolean third_double = moves_requested.get(2).tile.is_double();
            // can only place 2 consecutive doubles and one non-double if you have three tiles to place
            if (!first_double || !second_double || third_double)
                return false;
        }

        // now just verify that trains endings match the tile a player picked for them
        for (ComboPair one_pair : moves_requested) {
            boolean train_end_hand_tile_match = all_trains.get(one_pair.train_name).hand_tile_matches_end(one_pair.tile);
            if (!train_end_hand_tile_match)
                return false;
        }
        return true;
    }

    /* *********************************************************************
    Function Name: decide_on_max_tiles
    Purpose: decide how many max tiles a player can select (depends on whether
        he chooses to draw tiles or not)
    Parameters:
        moves_requested -> vector of <train name, tile to be placed> pairs
    Algorithm :
       - i set a flag of I am drawing specifically to determine how many tiles
          a human player can draw at once
    Return Value: true or false
    Help received: none
    ********************************************************************* */
    protected int decide_on_max_tiles() {
        if (this.after_drawing_move)
            return 1;
        else
            return 3;
    }


    /* *********************************************************************
    Function Name: calc_move_score
    Purpose: compares the best eligible combo for the current move.
            Best has the most points as a result (simple strategy)
    Parameters:
        vector<pair<string,Tile>> cur_combo -> current move
    Return Value:
    Help received: none
    ********************************************************************* */
    protected int calc_move_score(List<ComboPair> moves_requested) {
        int cur_score = 0;
        for (ComboPair cur_pair : moves_requested) {
            cur_score += cur_pair.tile.getLeft() + cur_pair.tile.getRight();
        }
        return cur_score;
    }

    /* *********************************************************************
    Function Name: place_tiles
    Purpose: place legal tiles on the train, deal with markers and orphan double tile
    Parameters:
        moves_requested -> vector of <train name, tile to be placed> pairs
    Algorithm:
       - order of loops matters in this function
       - first remove markers and previous orphans since at least one tile in the move
          is going on those trains
       - THEN add back doubles if a tile I am placing is a lonely double
       - finally, actually place tiles -> remove from hand and add to respective trains
    Return Value: true or false
    Help received: none
    ********************************************************************* */
    public void place_tiles(List<ComboPair> move_one_turn, Map<String, Train> all_trains) {
        // deal markers
        // if there's a marker on personal train, and you are placing a tile on your train -> remove the marker
        for (ComboPair cur_pair : move_one_turn){
            if (cur_pair.train_name.equals(this.name))
                Objects.requireNonNull(all_trains.get(this.name)).setMarker(false);
        }

        // removing previous orphan doubles
        // if the tile played by player is goes to train with orphan double, remove the orphan double
        for (ComboPair cur_pair : move_one_turn){
            if (Objects.requireNonNull(all_trains.get(cur_pair.train_name)).isEnds_with_orphan_double())
                Objects.requireNonNull(all_trains.get(cur_pair.train_name)).setEnds_with_orphan_double(false);
        }

        // adding new  orphan doubles
        // orphan double are created when double or triple are played: they activate when a double is the only tile played in a turn
        for (int i = 0; i < move_one_turn.size(); i++){
            String train_name = move_one_turn.get(i).train_name;
            Tile tile_to_place = move_one_turn.get(i).tile;
            // if the tile to be placed is double, check if the following tiles in the same move are placed on the same train
            if (tile_to_place.is_double()){
                boolean create_orphan_double = true;
                for (int j = i+1; j < move_one_turn.size(); j++) {
                    ComboPair next_pair = move_one_turn.get(i);
                    if (train_name.equals(next_pair.train_name))
                        create_orphan_double = false;
                }
                if (create_orphan_double)
                    Objects.requireNonNull(all_trains.get(train_name)).setEnds_with_orphan_double(true);
            }
        }

        // finally, remove tiles from the hand and add them to the train
        for (int i = 0; i < move_one_turn.size(); i++){
//            // find tile in your hand
//            int index_to_remove = hand.indexOf(move_one_turn.get(i).tile);
//            // remove tile from your hand
//            hand.remove(move_one_turn.get(i).tile);
            for (Iterator<Tile> iterator = hand.iterator(); iterator.hasNext();) {
                Tile iterating_tile = iterator.next();
                if (move_one_turn.get(i).tile.equals(iterating_tile)) {
                    iterator.remove();
                    break;
                }
            }
            // add this new tile to tht train
            Objects.requireNonNull(all_trains.get(move_one_turn.get(i).train_name)).add_new_tile(move_one_turn.get(i).tile);
        }
    }

    /* *********************************************************************
    Function Name: ask_for_help
    Purpose: returns the most optimal solution given current hand (used by computer as well)
    Parameters:
    Return Value:
        pair<string, vector<pair<string,Tile>>> -> first pair : string message, vector of the turn
    Algorithm:
       - first, get eligible tiles -> a tile with a respective train it can be placed it on
       - have 3 different series of nested loop: each series is fitted for specific number of tiles
          you have at hand
       - throughout these loops I'm looking exclusively for combo of tiles with most points. I keep the
          best and swap it for another combo of tiles if this combo has more points
    Help received: none
    ********************************************************************* */
    public List<ComboPair> ask_for_help (Map<String, Train> all_trains){
        this.deduce_eligible_trains(all_trains);

        List<ComboPair> mix_elig_tile = new ArrayList<>();

        // get vector of eligible tile with respective trains ->
        // simply going by if there are any number matches in eligible trains
        for (Tile hand_tile : this.hand) {

            for (Train train_only : all_trains.values()) {
                if (!train_only.isCurrent_eligible_train())
                    continue;
                if (!train_only.hand_tile_matches_end(hand_tile))
                    continue;
                mix_elig_tile.add(new ComboPair(train_only.getName(), new Tile(hand_tile.getLeft(), hand_tile.getRight())));
            }
        }

        // if nothing added, there are no more possible moves possible
        // mix_elig_tile is empty, so it's best to draw a tile
        if (mix_elig_tile.isEmpty())
            return mix_elig_tile;

        // at this point we know we have some number matches, but we need to find out
        // if they are legal based on rules AND which is the best one
        int max_points = -1;
        List<ComboPair> best_move = new ArrayList<>();
        // iterate once to see if there are possible plays
        for (int i = 0; i < mix_elig_tile.size(); i++) {
            // turn the first element as a vector of itself and pass to a verification function
            List<ComboPair> dummy = new ArrayList<>();
            dummy.add(mix_elig_tile.get(i));
            if (this.verify_tiles(dummy, all_trains)) {
                int new_move_score = this.calc_move_score(dummy);
                if (new_move_score > max_points) {
                    max_points = new_move_score;
                    best_move = dummy;
                }
            }
        }

        // iterate once to see if there are possible plays with two tile plays
        for (int tile_one = 0; tile_one < mix_elig_tile.size(); tile_one++) {
            // first num is always a double in move of two
            if (!mix_elig_tile.get(tile_one).tile.is_double()) {continue;}
            for (int tile_two = 0; tile_two < mix_elig_tile.size(); tile_two++) {
                // skip duplicating tiles
                if (tile_one == tile_two) {continue;}
                // turn the first element as a vector of itself and pass to a verification function
                List<ComboPair> dummy = new ArrayList<>();
                dummy.add(mix_elig_tile.get(tile_one));
                dummy.add(mix_elig_tile.get(tile_two));
                if (this.verify_tiles(dummy, all_trains)) {
                    int new_move_score = this.calc_move_score(dummy);
                    if (new_move_score > max_points) {
                        max_points = new_move_score;
                        best_move = dummy;
                    }
                }
            }
        }

        // iterate once to see if there are possible plays with three tile plays
        for (int tile_one = 0; tile_one < mix_elig_tile.size(); tile_one++) {
            // first num is always a double in move of three
            if (!mix_elig_tile.get(tile_one).tile.is_double()) {continue;}

            for (int tile_two = 0; tile_two < mix_elig_tile.size(); tile_two++) {
                // second num is always a double in move of three
                if (!mix_elig_tile.get(tile_two).tile.is_double()) {continue;}
                // skip duplicating tiles
                if (tile_one == tile_two) {continue;}

                for (int three = 0; three < mix_elig_tile.size(); three++) {
                    // skip duplicating tiles
                    if (tile_one == three || tile_two == three) { continue; }

                    // turn the first element as a vector of itself and pass to a verification function
                    List<ComboPair> dummy = new ArrayList<>();
                    dummy.add(mix_elig_tile.get(tile_one));
                    dummy.add(mix_elig_tile.get(tile_two));
                    dummy.add(mix_elig_tile.get(three));
                    if (this.verify_tiles(dummy, all_trains)) {
                        int new_move_score = this.calc_move_score(dummy);
                        if (new_move_score > max_points) {
                            max_points = new_move_score;
                            best_move = dummy;
                        }
                    }
                }
            }
        }

        return best_move;

    }


    /*
    GETTERS AND SETTERS + PRIVATE VARIABLES
     */

    /* *********************************************************************
    Function Name: getName
    Purpose: gets access to the name of the given player
    Parameters: none
    Return Value: string name
    Help received: none
    ********************************************************************* */
    public String getName() {
        return name;
    }


    /* *********************************************************************
    Function Name: isPlays_first
    Purpose: show the private member variable plays_first
    Parameters: none
    Return Value: boolean plays_first
    Help received: none
    ********************************************************************* */
    public boolean isPlays_first() {
        return plays_first;
    }

    /* *********************************************************************
    Function Name: setPlays_first
    Purpose: update the private member variable hand after having made a move
    Parameters: flag boolean
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void setPlays_first(boolean plays_first) {
        this.plays_first = plays_first;
    }


    /* *********************************************************************
    Function Name: isPlayer_cant_play
    Purpose: gets player_cant_play member variable
    Parameters: none
    Return Value: bool player_cant_play
    Help received: none
    ********************************************************************* */
    public boolean isPlayer_cant_play() {
        return player_cant_play;
    }


    /* *********************************************************************
    Function Name: setPlayer_cant_play
    Purpose: set if player can or cannot play
    Parameters: none
    Return Value: bool player_cant_play
    Help received: none
    ********************************************************************* */
    public void setPlayer_cant_play(boolean player_cant_play) {
        this.player_cant_play = player_cant_play;
    }


    /* *********************************************************************
    Function Name: getScore
    Purpose: selector function to retrieve score variable
    Parameters: none
    Return Value: score
    Help received: none
    ********************************************************************* */
    public int getScore() {
        return score;
    }


    /* *********************************************************************
    Function Name: setScore
    Purpose: selector function to update score variable
    Parameters:
        new_score -> new score
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void setScore(int score) {
        this.score = score;
    }


    /* *********************************************************************
    Function Name: getHand
    Purpose: returns hand (for saving purposes)
    Parameters: none
    Return Value: vector of tiles
    Help received: none
    ********************************************************************* */
    public List<Tile> getHand() {
        return hand;
    }


    /* *********************************************************************
    Function Name: setHand
    Purpose: initializes the hand of the player at the start of the round
    Parameters:
        new_hand -> 16 tiles
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void setHand(List<Tile> hand) {
        this.hand = hand;
    }


    /* *********************************************************************
    Function Name: isSave_game_request
    Purpose: returns whether a player requested to save a game
    Parameters: none
    Return Value: bool player_cant_play
    Help received: none
    ********************************************************************* */
    public boolean isSave_game_request() {
        return save_game_request;
    }

    // variables
    protected String name;
    protected boolean plays_first;
    protected boolean player_cant_play;
    protected int score;
    protected List<Tile> hand = new ArrayList<>();
    protected boolean after_drawing_move;
    protected boolean save_game_request;


}
