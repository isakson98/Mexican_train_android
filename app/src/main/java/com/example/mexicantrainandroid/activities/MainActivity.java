package com.example.mexicantrainandroid.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mexicantrainandroid.R;
import com.example.mexicantrainandroid.models.Computer;
import com.example.mexicantrainandroid.models.Player;
import com.example.mexicantrainandroid.models.Serialization;
import com.example.mexicantrainandroid.models.Tile;
import com.example.mexicantrainandroid.models.Train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * The starting activity of the app
 *
 * It allows to start new game, load a game, or exit
 *
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String m_file_name_text = new String();
    Serialization ser_obj = new Serialization();

    Player human = new Player("Human");
    Computer computer = new Computer("Computer");

    Map<String, Train> all_trains = new HashMap<String, Train>();

    int round_number = 1;

    @Override
    /**
     * function that deals with all button responses
     * @param v
     */
    public void onClick(View v) {
        String ToastMessage = "None";
        switch (v.getId()) {
            case R.id.playNewGameButton:
                ToastMessage = "New Game starting";
                onNewGameButtonClick();
                break;
            case R.id.playSavedGameButton:
                ToastMessage = "Save Game starting";
                onSavedButtonClick(v);
                break;
            case R.id.exitGameButton:
                ToastMessage="Exiting";
                onExitButtonClick(v);
                break;
        }
        Toast.makeText(this, ToastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    /**
     * initializes button listeners as the activity is starting
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play_button = findViewById(R.id.playNewGameButton);
        play_button.setOnClickListener(this);

        Button load_button = findViewById(R.id.playSavedGameButton);
        load_button.setOnClickListener(this);

        Button exit_button = findViewById(R.id.exitGameButton);
        exit_button.setOnClickListener(this);


    }

    /**
     * new button adds round, human, computer, and a marker whether a game passed
     * is a game or not
     */
    public void onNewGameButtonClick() {

        Intent newGameIntent = new Intent(this, RoundActivity.class);
        newGameIntent.putExtra("Round", round_number);
        newGameIntent.putExtra("Human", human);
        newGameIntent.putExtra("Computer", computer);
        newGameIntent.putExtra("Loaded Game", false);
        startActivity(newGameIntent);
    }


    /**
     * saved button prompts a file to load, adds additional details to intent, and starts the round activity
     * Help received: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
     * @param saveButton
     */
    public void onSavedButtonClick(View saveButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Loading saved file");
        builder.setMessage("Please enter file name to load");

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_file_name_text = input.getText().toString();
                AssetManager assetManager = getAssets();
                // verify file exists
                boolean loading_ok = ser_obj.load_data(assetManager, m_file_name_text, human, computer, all_trains);
                if (!loading_ok){
                    String ToastMessage = "Input is invalid! Try again";
                    Toast.makeText(MainActivity.this, ToastMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                // if it does load all its content into respective data structures

                // start the game
                Intent saveGameIntent = new Intent(MainActivity.this, RoundActivity.class);
                saveGameIntent.putExtra("Round", ser_obj.getRound_number());
                saveGameIntent.putExtra("Human", human);
                saveGameIntent.putExtra("Computer", computer);
                saveGameIntent.putExtra("Human Train", all_trains.get("Human"));
                saveGameIntent.putExtra("Computer Train", all_trains.get("Computer"));
                saveGameIntent.putExtra("Mexican Train", all_trains.get("Mexican"));
                saveGameIntent.putExtra("Boneyard",  (ArrayList) ser_obj.getBoneyard_tiles());
                saveGameIntent.putExtra("Loaded Game", true);
                startActivity(saveGameIntent);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    /**
     * exiting the game
     * @param exiButton
     */
    public void onExitButtonClick(View exiButton) {
        System.exit(1);
        System.out.println("Exiting");
    }

}