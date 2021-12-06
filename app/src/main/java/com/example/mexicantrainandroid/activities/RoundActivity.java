package com.example.mexicantrainandroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mexicantrainandroid.R;
import com.example.mexicantrainandroid.models.ComboPair;
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
                // update current table?

                // verify if game ended

                // let computer move
                // update current table?

                // verify if game ended

                // if game ended


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
        computer = (Player) getIntent().getSerializableExtra("Computer");

        int start_denom = this.determine_starting_denomination();

        if (game_is_loaded) {
            all_trains.put("Human", (Train) getIntent().getSerializableExtra("Human Train"));
            all_trains.put("Computer", (Train) getIntent().getSerializableExtra("Computer Train"));
            all_trains.put("Mexican", (Train) getIntent().getSerializableExtra("Mexican Train"));
            boneyard = (List<Tile>) getIntent().getSerializableExtra("Boneyard");
        }
        // if not loaded, need to initialize the game
        else {
            // distribute tiles
            Deck new_deck = new Deck();
            human.setHand(new_deck.takeOutBunchTiles(16));
            computer.setHand(new_deck.takeOutBunchTiles(16));
            boneyard = new_deck.takeOutBunchTiles(-1);

            //start trains
            all_trains.put("Human", new Train("Human", new Tile(start_denom, start_denom)));
            all_trains.put("Computer", new Train("Computer", new Tile(start_denom, start_denom)));
            all_trains.put("Mexican", new Train("Mexican", new Tile(start_denom, start_denom)));

        }

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

        // allow option to skip round

        // let computer make a move

        // display computer

    }

    /* *********************************************************************
    Function Name: onAskHelpButtonClick
    Purpose: exits game
    Parameters:
    Return Value: integer
    Help received: none
    ********************************************************************* */
    public void onAskHelpButtonClick() {
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
    Player computer = new Player("Computer");
    int round_number;
    List<Tile> boneyard;
    Map<String, Train> all_trains = new HashMap<String, Train>();

}
