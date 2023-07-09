package com.example.wronglane;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Ranks extends AppCompatActivity {
public storeData sd;
ArrayList<String> scores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranks);
        sd=new storeData(this);
        scores=sd.getData();
        TextView scoresTextView = findViewById(R.id.rank_list);
        StringBuilder sb = new StringBuilder();
        int mn=Integer.min(scores.size(),10);
        for (int i = 0; i < mn; i++) {
            int position = i + 1;
            int score = Integer.parseInt(scores.get(i));
            sb.append("Rank ").append(position).append(":   ").append(score).append("\n");
        }
        scoresTextView.setText(sb.toString());
    }
}