package com.example.mexicantrainandroid.models;


/*

This class primarily focuses data translation from text to data structures and vice versa.

 */

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serialization {



    /* *********************************************************************
    Function Name: load_data
    Purpose: loads data ->
    Parameters:none
    Return Value: boolean
    Help received: none
    ********************************************************************* */
    public boolean load_data(AssetManager manager, String file_name, Player human, Player computer, Map<String, Train> all_trains) {

        this.human_plr = human;
        this.comp_plr = computer;
        this.all_trains = all_trains;


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(manager.open(file_name), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                System.out.println(mLine);
                int colon_pos = mLine.indexOf(":");
                // disregard the line with no colon
                if (colon_pos == -1) { continue; }

                // parse the keyword
                String key_word = mLine.substring(0, colon_pos);

                key_word = this.strip_junk(key_word);

                if (key_word.equals("Computer") || key_word.equals("Human")) {
                    this.cur_parsed_playing = key_word;
                }

                this.parse_by_key(key_word, colon_pos, mLine);

            }
        } catch (IOException e) {
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /* *********************************************************************
    Function Name: strip_junk
    Purpose: removes spaces from the string, passed by reference!
    Parameters: const string& number -> used heavily
    Return Value: none (change done in the string passed by reference)
    Help received: none
    ********************************************************************* */
    private String strip_junk(String key_word){
        key_word = key_word.replaceAll("[^a-zA-Z0-9]+", " ");
        key_word = key_word.replaceAll(" ", "");
        return key_word;
    }


    /* *********************************************************************
    Function Name: parse_by_key
    Purpose: parses by key,
    Parameters:
        string key_word -> key word LHS of the first line
        int line_num -> line number in lines, so I don't have to look for it
        int colon_pos -> position where the colon is
        vector<string>& lines -> passed by reference
    Return Value: none (change done in the string passed by reference)
    Help received: none
    ********************************************************************* */
    private void parse_by_key(String key_word, int colon_pos, String full_string) {

        String past_colon_string = full_string.substring(colon_pos+1);

        past_colon_string = this.strip_junk(past_colon_string);

        // since I deleted white space I am checking strictly for
        // letters all into one piece, thus "NextPlayer", without space in between
        if (key_word.equals("Round")) {
            this.round_number = this.parse_number(past_colon_string);
        }
        else if (key_word.equals( "Score")) {
            int score = this.parse_number(past_colon_string);
            if (this.cur_parsed_playing.equals("Human")) {human_plr.setScore(score);}
            else if (this.cur_parsed_playing.equals("Computer")) {comp_plr.setScore(score);}
        }
        else if (key_word.equals( "Hand")) {
            if (this.cur_parsed_playing.equals("Human")) {human_plr.setHand((this.parse_bunch_tiles(past_colon_string, false)));}
            else if (this.cur_parsed_playing.equals("Computer")) {comp_plr.setHand((this.parse_bunch_tiles(past_colon_string, false)));};
        }
        else if (key_word.equals("Train")) {
            Train player = new Train(this.cur_parsed_playing);
            this.all_trains.put(this.cur_parsed_playing, player);
            boolean left_to_right = true;
            if (this.cur_parsed_playing.equals("Computer")) {left_to_right=false;}
            past_colon_string = this.check_for_markers(past_colon_string);
            this.all_trains.get(this.cur_parsed_playing).setTrain_tiles(this.parse_bunch_tiles(past_colon_string, left_to_right));
        }
        else if (key_word.equals("MexicanTrain")) {
            // adding tiles to train from parsed string in the line
            Train mexican = new Train("Mexican");
            this.all_trains.put("Mexican", mexican);
            this.all_trains.get("Mexican").setTrain_tiles(this.parse_bunch_tiles(past_colon_string, true));
        }
        else if (key_word.equals("Boneyard")) {
            this.boneyard_tiles = this.parse_bunch_tiles(past_colon_string, false);
        }
        else if (key_word.equals( "NextPlayer" )) {
            if (past_colon_string.equals("Human")) {this.human_plr.setPlays_first(true);}
            if (past_colon_string.equals("Computer")) {this.comp_plr.setPlays_first(true);}
        }

    }



    /* *********************************************************************
    Function Name: parse_number
    Purpose: converts string version of the number to integer
    Parameters: const string& number -> string, a score
    Return Value: int
    Help received: none
    ********************************************************************* */
    private int parse_number(String past_colon_string) {

        return Integer.parseInt(past_colon_string);

    }

    /* *********************************************************************
    Function Name: parse_bunch_tiles
    Purpose: parse string of tiles into actual vector of tile objects
    Parameters:
        const string& number -> string, a score
        bool left_to_right -> how the string itself is formatted and where you need to start reading from
    Return Value: vector of tile
    Help received: none
    ********************************************************************* */
    private  List<Tile> parse_bunch_tiles(String past_colon_string, boolean left_to_right) {

        if (!left_to_right){
            past_colon_string = new StringBuilder(past_colon_string).reverse().toString();
        }

        List<Tile> bunch_tiles = new ArrayList<Tile>();

        for (int i = 0; i < past_colon_string.length(); i = i+2) {
            int one_side = this.parse_number(past_colon_string.substring(i,i+1));
            int other_side = this.parse_number(past_colon_string.substring(i+1,i+2));
            bunch_tiles.add(new Tile(one_side,other_side));
        }
        return bunch_tiles;
    }


    /* *********************************************************************
    Function Name: check_for_markers
    Purpose: checks for Ms and removes them for parsing purposes
        also marks the train is having a marker
    Parameters: const string& number -> string
    Return Value: none -> done on a reference
    Help received: none
    ********************************************************************* */
    private String check_for_markers(String past_colon_string){
        if (  past_colon_string.charAt(0) == 'M' ) {
            past_colon_string = past_colon_string.substring(1);
            this.all_trains.get(this.cur_parsed_playing).setMarker(true);
        }
        else if ( past_colon_string.charAt(past_colon_string.length()-1) == 'M'  ) {
            past_colon_string = past_colon_string.substring(0,past_colon_string.length()-1 );
            this.all_trains.get(this.cur_parsed_playing).setMarker(true);
        }
        return past_colon_string;
    }



    private String cur_parsed_playing;

    // data that is shared
    int round_number;
    Player human_plr;
    Player comp_plr;

    Map<String, Train> all_trains = new HashMap<String, Train>();

    List<Tile> boneyard_tiles = new ArrayList<Tile>();


}
