package com.example.mexicantrainandroid.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    // not needed in Java
    // virtual void play(unordered_map<string,Train>&, vector<Tile>&);
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
    private void deduce_eligible_trains(HashMap<String,Train> all_trains) {
        // reinitialize to have all trains to be ineligible at the start of a player's turn
        for (Train single : all_trains.values()) {
            single.setCurrent_eligible_train(false);
        }

        boolean orphans_present = false;
//        /*
//         * first find orphan double trains, can't play other trains until you get these sorted
//         */
//        for (Map.Entry<String, Train> entry : all_trains.entrySet()) {
//            if (entry.getValue().isEnds_with_orphan_double()) {
//                orphans_present = true;
//                all_trains.set
//            }
//        }


    }
//    bool draw_from_boneyard(vector<Tile>&, unordered_map<string,Train>&);
//    bool verify_drawn_tile_playable(unordered_map<string,Train>&);
//    bool verify_tiles(vector<pair<string, Tile>>, unordered_map<string,Train>&);
//    void place_tiles(vector<pair<string, Tile>>, unordered_map<string,Train>&);
//    int decide_on_max_tiles() const;
//    unsigned int calc_move_score(vector<pair<string,Tile>>);
//    vector<pair<string,Tile>> ask_for_help(unordered_map<string,Train>&);


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
