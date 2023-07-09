package com.example.wronglane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameEnd extends AppCompatActivity {
    TextView dispScore;
    public storeData sd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        int score = getIntent().getExtras().getInt("score");
        dispScore = findViewById(R.id.dispScore);
        dispScore.setText("" + score);
        sd=new storeData(this);
        sd.addData(score);
    }

    public void startGame(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void Home(View view) {
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }
}