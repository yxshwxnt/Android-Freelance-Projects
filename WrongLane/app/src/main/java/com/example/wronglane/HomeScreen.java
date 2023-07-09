package com.example.wronglane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class HomeScreen extends AppCompatActivity {
    private MediaPlayer gameStart;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        gameStart=MediaPlayer.create(this,R.raw.sta);
    }
    public void startGame(View view) {
        if(gameStart!=null){
            gameStart.start();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void exitGame(View view) {
        finish();
        System.exit(0);
    }

    public void showRanks(View view) {
        startActivity(new Intent(this, Ranks.class));
    }
}