package com.example.mexicantrainandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mexicantrainandroid.R;

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

        Intent intent = getIntent();

//        initialize buttons
//        Button save_button = (Button) findViewById(R.id.saveGameButton);
//        save_button.setOnClickListener(this);
//
//        Button move_button = (Button) findViewById(R.id.makeMoveButton);
//        move_button.setOnClickListener(this);
//
//        Button help_button =(Button)  findViewById(R.id.askHelpButton);
//        help_button.setOnClickListener(this);
//
//        Button quit_button = (Button) findViewById(R.id.quitButton);
//        quit_button.setOnClickListener(this);

//        initialize round components -> players


    }
}
