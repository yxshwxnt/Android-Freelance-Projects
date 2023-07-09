package com.example.hwproject;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hwproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class topScore extends AppCompatActivity {
    public connectDB helper;
    public ListView topScoreListView;
    public ArrayList<String> top10list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_score);
        helper=new connectDB(this);

        top10list = helper.getTop10();
        showData(top10list);
    }

    public void showMainActivity(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void showData(ArrayList<String> scores){
        TextView scoresTextView = findViewById(R.id.top_score_list);
        StringBuilder sb = new StringBuilder();
        int mn=Integer.min(scores.size(),10);
        for (int i = 0; i < mn; i++) {
            int position = i + 1;
            int score = Integer.parseInt(scores.get(i));
            sb.append(position);
            switch (position){
                case 1:
                    sb.append("st:    Score ").append(score).append("\n");
                    break;
                case 2:
                    sb.append("nd:    Score ").append(score).append("\n");
                    break;
                case 3:
                    sb.append("rd:    Score ").append(score).append("\n");
                    break;
                default:
                    sb.append(":    Score ").append(score).append("\n");
                    break;
            }
        }
        scoresTextView.setText(sb.toString());
    }
}