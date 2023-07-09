package com.example.spaceshooter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    TextView tvPoints;
    private StoreScore s;
    public Score sc;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s=new StoreScore(this);
        setContentView(R.layout.game_over);
        int points = getIntent().getExtras().getInt("points");
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText("" + points);
        s.addScore(points);
        sc=new Score(points);
    }
    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, StartUp.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        finish();
    }

    public void showTop10(View view) {
        Intent intent=new Intent(GameOver.this,Positions.class);
        startActivity(intent);
    }

    public void home(View view) {
        Intent intent=new Intent(GameOver.this,StartUp.class);
        startActivity(intent);
        finish();
    }
}
