package com.example.mexicantrainandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mexicantrainandroid.R;


/*
* interface -> must have the methods specified by everyone using it
*
* abstract -> cannot have multiple inhertiance (several abstract class)
*          -> cannot have
*
* https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html
*
* */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    // view an button are equivalent
    // with this structure, just one function is needed
    public void onClick(View v) {
        String ToastMessage = "None";
        switch (v.getId()) {
            case R.id.playNewGameButton:
                ToastMessage = "New Game starting";
                Intent saveGameIntent = new Intent(this, RoundActivity.class);
                startActivity(saveGameIntent);
                break;
            case R.id.playSavedGameButton:
                ToastMessage = "Save Game starting";
                break;
            case R.id.exitGameButton:
                ToastMessage="Exiting";
                onExitButtonClick(v);
                break;
        }
        Toast.makeText(this, ToastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
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

    /*
    Exiting the game
     */
    public void onExitButtonClick(View exiButton) {
        System.exit(1);
        System.out.println("Exiting");
    }

}