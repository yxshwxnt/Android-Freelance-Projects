package com.example.atari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameEnd extends AppCompatActivity {
    public String points;
    private TextView displPoints;
    public addPoints l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        l=new addPoints(GameEnd.this);

        points=getIntent().getExtras().get("Points").toString();
        displPoints=(TextView) findViewById(R.id.displayScore);
        displPoints.setText("Current Game Score: "+points);
        l.addPts(Integer.parseInt(points));
    }
    public void dispLeaderboard(View view){
        Intent leaderboard;
        leaderboard = new Intent(this,Leaderboard.class);
        startActivity(leaderboard);
    }
    public void goHome(View view){
        Intent Home;
        Home = new Intent(this,MainActivity.class);
        startActivity(Home);
        finish();
    }
    public void exitGame(View view){
        finish();
        System.exit(0);
    }
}