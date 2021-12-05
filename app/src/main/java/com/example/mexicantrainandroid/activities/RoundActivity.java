package com.example.mexicantrainandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mexicantrainandroid.R;
import com.example.mexicantrainandroid.models.Player;
import com.example.mexicantrainandroid.models.Serialization;
import com.example.mexicantrainandroid.models.Tile;
import com.example.mexicantrainandroid.models.Train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundActivity extends Activity implements View.OnClickListener{
    @Override
    // view an button are equivalent
    // with this structure, just one function is needed
    public void onClick(View v) {
        String ToastMessage = "None";
        switch (v.getId()) {
            case R.id.saveGameButton:
                ToastMessage = "New Game starting";
                break;
            case R.id.makeMoveButton:
                ToastMessage = "Save Game starting";
                break;
            case R.id.askHelpButton:
                ToastMessage="Exiting";
                break;
        }
        Toast.makeText(this, ToastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity);

        // initialize round components -> players
        Intent intent = getIntent();

        this.unpack_intent_extras(intent);


        // initialize buttons
        Button save_button = (Button) findViewById(R.id.saveGameButton);
        save_button.setOnClickListener(this);

        Button move_button = (Button) findViewById(R.id.makeMoveButton);
        move_button.setOnClickListener(this);

        Button help_button =(Button)  findViewById(R.id.askHelpButton);
        help_button.setOnClickListener(this);

        Button quit_button = (Button) findViewById(R.id.quitButton);
        quit_button.setOnClickListener(this);


    }


    public void unpack_intent_extras(Intent intent) {

        boolean game_is_loaded = (boolean) getIntent().getSerializableExtra("Loaded Game");

        round_number = (int) getIntent().getSerializableExtra("Round");
        human = (Player) getIntent().getSerializableExtra("Human");
        computer = (Player) getIntent().getSerializableExtra("Computer");

        if (game_is_loaded) {
            all_trains.put("Human", (Train) getIntent().getSerializableExtra("Human Train"));
            all_trains.put("Computer", (Train) getIntent().getSerializableExtra("Computer Train"));
            all_trains.put("Mexican", (Train) getIntent().getSerializableExtra("Mexican Train"));
//            boneyard =  getIntent().getIntegerArrayListExtra("Boneyard", (ArrayList<Tile>) ser_obj.getBoneyard_tiles());
        }
        // if not loaded, need to initialize the game
        else {
            // distribute tiles
        }

    }

    Serialization ser_obj = new Serialization();
    Player human = new Player("Human");
    Player computer = new Player("Computer");
    int round_number;
    List<Tile> boneyard;
    Map<String, Train> all_trains = new HashMap<String, Train>();

}
