package com.example.mexicantrainandroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mexicantrainandroid.R;
import com.example.mexicantrainandroid.models.ComboPair;
import com.example.mexicantrainandroid.models.Computer;
import com.example.mexicantrainandroid.models.Deck;
import com.example.mexicantrainandroid.models.Player;
import com.example.mexicantrainandroid.models.Serialization;
import com.example.mexicantrainandroid.models.Tile;
import com.example.mexicantrainandroid.models.Train;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundActivity extends Activity implements View.OnClickListener{
    @Override
    // view an button are equivalent
    // with this structure, just one function is needed
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveGameButton:

                // start serializaiton object
                Serialization ser_obj = new Serialization();
                try {
                    ser_obj.save_game(this.round_number, this.human, this.computer, this.all_trains, this.boneyard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // gather all the info and exit
                onExitButtonClick();
                break;

            case R.id.placeTilesButton:
                // human makes a move
                onMakeMoveButtonClick(v);

                break;
            case R.id.drawTiles:
                // human makes a move
                onDrawTileButtonClick(v);

                break;
            case R.id.askHelpButton:
                // show pop to ask for help -> maybe so it doesn't dissapear
                onAskHelpButtonClick();

                break;

            case R.id.quitButton:
                onExitButtonClick();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity);

        // initialize round components -> players
        Intent intent = getIntent();

        this.init_round_info(intent);


        // initialize buttons
        Button save_button = (Button) findViewById(R.id.saveGameButton);
        save_button.setOnClickListener(this);

        Button move_button = (Button) findViewById(R.id.placeTilesButton);
        move_button.setOnClickListener(this);

        Button draw_button = (Button) findViewById(R.id.drawTiles);
        draw_button.setOnClickListener(this);

        Button help_button =(Button)  findViewById(R.id.askHelpButton);
        help_button.setOnClickListener(this);

        Button quit_button = (Button) findViewById(R.id.quitButton);
        quit_button.setOnClickListener(this);

        // display round
        TextView roundTextView = (TextView) findViewById(R.id.textViewRound);
        String full_round_text = roundTextView.getText() + String.valueOf(this.round_number);
        roundTextView.setText(full_round_text);
        // set computer score
        TextView compScoreTextView = (TextView) findViewById(R.id.textViewCompScor);
        String full_comp_score_text = compScoreTextView.getText() + String.valueOf(this.computer.getScore());
        compScoreTextView.setText(full_comp_score_text);
        // set human score
        TextView humanScoreTextView = (TextView) findViewById(R.id.textViewHumanScor);
        String full_human_score_text = humanScoreTextView.getText() + String.valueOf(this.human.getScore());
        humanScoreTextView.setText(full_human_score_text);


        displayAllTiles();


    }


    /* *********************************************************************
    Function Name: init_round_info
    Purpose: at the start of round, this function distributes tiles to
            players and sets aside the rest for the boneyard
    Parameters:
        Intent intent -> info carried from previous activity with starting values
    Return Value: none
    Algorithm: none
    ********************************************************************* */
    public void init_round_info(Intent intent) {

        boolean game_is_loaded = (boolean) getIntent().getSerializableExtra("Loaded Game");
        round_number = (int) getIntent().getSerializableExtra("Round");
        human = (Player) getIntent().getSerializableExtra("Human");
        computer = (Computer) getIntent().getSerializableExtra("Computer");

        if (game_is_loaded) {
            all_trains.put("Human", (Train) getIntent().getSerializableExtra("Human Train"));
            if (all_trains.get("Human").getLastTile().is_double())
                all_trains.get("Human").setEnds_with_orphan_double(true);
            all_trains.put("Computer", (Train) getIntent().getSerializableExtra("Computer Train"));
            if (all_trains.get("Computer").getLastTile().is_double())
                all_trains.get("Computer").setEnds_with_orphan_double(true);
            all_trains.put("Mexican", (Train) getIntent().getSerializableExtra("Mexican Train"));
            if (all_trains.get("Mexican").getLastTile().is_double())
                all_trains.get("Mexican").setEnds_with_orphan_double(true);
            boneyard = (List<Tile>) getIntent().getSerializableExtra("Boneyard");
        }
        // if not loaded, need to initialize the game
        else {
            start_new_round();
        }

    }

    /* *********************************************************************
    Function Name: init_round_info
    Purpose: at the start of round, this function distributes tiles to
            players and sets aside the rest for the boneyard
    Parameters:
        Intent intent -> info carried from previous activity with starting values
    Return Value: none
    Algorithm: none
    ********************************************************************* */
    public void start_new_round() {
        int start_denom = this.determine_starting_denomination();
        // distribute tiles
        Deck new_deck = new Deck();
        Tile starting_tile = new_deck.takeOutDoubleTiles(start_denom);
        human.setHand(new_deck.takeOutBunchTiles(16));
        computer.setHand(new_deck.takeOutBunchTiles(16));
        boneyard = new_deck.takeOutBunchTiles(-1);

        //start trains
        all_trains.put("Human", new Train("Human", starting_tile));
        all_trains.put("Computer", new Train("Computer", starting_tile));
        all_trains.put("Mexican", new Train("Mexican", starting_tile));

    }




    /* *********************************************************************
    Function Name: determine_engine_tile
    Purpose: determines the starting tile of the game based on round number
    Return Value: unsigned int starting_double
    Algorithm: number of starting double follows a nice pattern being
        10 - one's digit of the number.
    Help received: none
    ********************************************************************* */
    private int determine_starting_denomination() {
        int starting_double = 10 - (this.round_number % 10);
        if (starting_double == 10) {starting_double = 0;}
        return starting_double;
    }


    /* *********************************************************************
    Function Name: onMakeMoveButtonClick
    Purpose: makes a move button
    Parameters:
        View moveButton
    Return Value: integer
    Help received: none
    ********************************************************************* */
    public void onMakeMoveButtonClick(View moveButton) {

        // this will be human making a move ->

        // having clicked on the needed tile buttons

        // this button will show all tiles clicked and all you have to do is confirm it


        this.after_human_move();

    }


    /* *********************************************************************
    Function Name: onDrawTileButtonClick
    Purpose: draw tiles
    Parameters:
        View moveButton
    Return Value: integer
    Help received: none
    ********************************************************************* */
    public void onDrawTileButtonClick(View drawButton) {

        // see if current hand is ok -> TODO lock the ability to draw after doing it once in a turn
        List<ComboPair> possible_move = human.ask_for_help(all_trains);
        String Message = "You cannot draw because you can still make a move";
        // cannot draw because certain moves are available
        if (drawn_this_turn) {
            Message = "You already drew this turn!";
        }
        else if (possible_move.size() != 0){
            // show message to draw
            Message = "You cannot draw because you can still make a move";
        }
        // human draws
        else {
            // drew something
            if (this.human.draw_from_boneyard(boneyard, all_trains)) {
                // drawing was sucessful -> show the
                Tile drawn_tile = this.human.getHand().get(this.human.getHand().size() - 1);
                Message = "You drew " +  String.valueOf(drawn_tile.getLeft()) + " - " + String.valueOf(drawn_tile.getLeft()) + "\n";
                // check if tile is playable
                if (this.human.verify_drawn_tile_playable(all_trains)) {
                    Message += "This tile is playable, so please place it on the appropriate train";
                    drawn_this_turn = true;
                }
                else {
                    Message += "This tile is not playable, so you skip this turn!";
                    this.after_human_move();
                }
                drawn_this_turn = true;
            }
            // boneyard is empty
            else {
                Message = "Boneyard is empty! Cannot draw";
                this.human.setPlayer_cant_play(true);
                this.after_human_move();
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Message)
                .setTitle("Drawing message");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    /* *********************************************************************
    Function Name: onAskHelpButtonClick
    Purpose: exits game
    Parameters:
    Return Value: integer
    Help received: none
    ********************************************************************* */
    public void onAskHelpButtonClick() {
        // TODO : lock the ability to ask for help after drawing
        List<ComboPair> possible_move = human.ask_for_help(all_trains);

        String Message = "None";
        StringBuilder advice_composition = new StringBuilder();
        if (possible_move.size() == 0){
            // show message to draw
            advice_composition.append("You don't have playable tiles!\nYou have to draw");
        }
        else {
            // show message of which tiles to draw
            advice_composition.append("To maximize point reduction: \n");
            int count = 1;
            for (ComboPair pair: possible_move) {
                String string_tile = String.valueOf(pair.tile.getLeft()) + "-" + String.valueOf(pair.tile.getRight());
                String single_placement = String.valueOf(count) + ". " + pair.train_name + " " + string_tile + "\n";
                advice_composition.append(single_placement);
                count += 1;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(advice_composition.toString())
                .setTitle("Advice message");

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(600, 600);
        alertDialog.show();

    }


    /* *********************************************************************
    Function Name: onExitButtonClick
    Purpose: exits game
    Parameters:
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void onExitButtonClick() {
        System.exit(1);
        System.out.println("Exiting");
    }

    /* *********************************************************************
    Function Name: after_human_move
    Purpose: exits game
    Parameters:
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void after_human_move() {
        // reset certain variables
        drawn_this_turn = false;

        // verify if game ended after human move
        if (this.round_ended()) {
            this.new_round_prompt();
        }
        // let computer move
        computer.play(boneyard, all_trains);

        // update current display
        this.displayAllTiles();

        // verify if game ended after computer player
        if (this.round_ended()) {
            this.new_round_prompt();
        }


    }

    /* *********************************************************************
    Function Name: round_ended
    Purpose: end round
    Parameters:
    Return Value: none
    Help received: none
    ********************************************************************* */
    public boolean round_ended() {

        // both skipped a turn
        if (human.isPlayer_cant_play() && computer.isPlayer_cant_play()) {
            return true;
        }

        // someone won
        if (human.hand_is_empty() || computer.hand_is_empty()) {
            return true;
        }

        return false;
    }

    /* *********************************************************************
    Function Name: new_round_prompt
    Purpose: ask if you want to play a new round of the same game
    Parameters:
    Return Value: none
    Help received: none
    ********************************************************************* */
    public void new_round_prompt() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Play another round");

        // Set up the buttons
        builder.setPositiveButton("Yes :)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // verify file exists
                round_number += 1;
                start_new_round();
                displayAllTiles();

            }
        });
        builder.setNegativeButton("No :(", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                String ToastMessage = "Thank you for playing!";
                Toast.makeText(RoundActivity.this, ToastMessage, Toast.LENGTH_SHORT).show();
                onExitButtonClick();
            }
        });

        builder.show();

    }





    /* *********************************************************************
    Function Name: displayAllTiles
    Purpose: shows tiles for everyone
    Parameters:
    Return Value: none
    Help received:  none
    ********************************************************************* */
    public void displayAllTiles() {

        // Computer
        this.displayHorizontalTiles(computer.getHand(), R.id.horizontalCompHandLayout);
        this.displayHorizontalTiles(this.all_trains.get("Computer").getTrain_tiles(), R.id.horizontalCompTrainLayout);

        // Human
        // TODO : make human hand tiles usable -> add feature to select which train to place it on
        this.displayHorizontalTiles(human.getHand(), R.id.horizontalHumanHandLayout);
        this.displayHorizontalTiles(this.all_trains.get("Human").getTrain_tiles(), R.id.horizontalHumanTrainLayout);

        // Mexican
        this.displayHorizontalTiles(this.all_trains.get("Mexican").getTrain_tiles(), R.id.horizontalMexicanTrainLayout);

        // Boneyard
        this.displayHorizontalTiles(this.boneyard, R.id.horizontalBoneyardLayout);

    }


    /* *********************************************************************
    Function Name: displayAllTiles
    Purpose: shows tiles for everyone
    Parameters:
    Return Value: none
    Help received: https://stackoverflow.com/questions/62775035/add-elements-in-a-horizontal-scroll-view-in-android-studio
    ********************************************************************* */
    public void displayHorizontalTiles(List<Tile> tiles_displaying, int layoutIdNumber) {
        // show user comp hand
        LinearLayout a = (LinearLayout) findViewById(layoutIdNumber);
        //added an ArrayList to store the Buttons if you want to use it later.
        ArrayList<Button> listOfButtons = new ArrayList<Button>();

        int count = 0;
        for(Tile comp_hand_tile : tiles_displaying) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            Button other_button = new Button(getApplicationContext());
            String tile_str = comp_hand_tile.getLeft() + " - " + comp_hand_tile.getRight();
            other_button.setText(tile_str);
            other_button.setId(View.generateViewId());
            listOfButtons.add(other_button);
            a.addView(other_button,layoutParams);
            count++;
        }
    }



    Serialization ser_obj = new Serialization();
    Player human = new Player("Human");
    Computer computer = new Computer("Computer");
    int round_number;
    List<Tile> boneyard;
    Map<String, Train> all_trains = new HashMap<String, Train>();
    // turn variables
    boolean drawn_this_turn;

}
