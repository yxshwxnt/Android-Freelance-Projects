package com.example.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Positions extends AppCompatActivity {
    public StoreScore s;
    ArrayList<String>  scoreArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);
        s=new StoreScore(this);
        scoreArr=s.scoreArr();

        TextView scoresTextView = findViewById(R.id.scores_text_view);
        StringBuilder sb = new StringBuilder();
        int mn=Integer.min(scoreArr.size(),10);
        for (int i = 0; i < mn; i++) {
            int position = i + 1;
            int score = Integer.parseInt(scoreArr.get(i));
            sb.append("Position ").append(position).append(":    Score ").append(score).append("\n");
        }
        scoresTextView.setText(sb.toString());
    }
}