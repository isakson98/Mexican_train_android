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
import java.util.List;

public class Serialization {



    /* *********************************************************************
    Function Name: load_data
    Purpose: loads data ->
    Parameters:none
    Return Value: boolean
    Help received: none
    ********************************************************************* */
    public boolean load_data(AssetManager manager, String file_name) {


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(manager.open(file_name), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                System.out.println(mLine);
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

    int loaded_round_num;
    Player human_plr;
    Player comp_plr;

    Train mexican;
    Train human;
    Train computer;

    List<Tile> boneyard_tiles = new ArrayList<Tile>();


}
