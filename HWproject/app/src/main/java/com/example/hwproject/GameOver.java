package com.example.hwproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameOver extends AppCompatActivity {
    TextView tvScore;
    public connectDB helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        helper=new connectDB(this);
        int score=getIntent().getExtras().getInt("score");
        tvScore=findViewById(R.id.tvScore);
        tvScore.setText(""+score);
        helper.appendScore(score);
    }

    public void restart(View view) {
        Intent intent=new Intent(GameOver.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showTopScore(View view) {
        Intent intent=new Intent(this,topScore.class);
        startActivity(intent);
        finish();
    }
    public void exitGame(View view) {
        finish();
        System.exit(0);
    }
}
