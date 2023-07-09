package com.example.savethefish;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class topScores extends AppCompatActivity {

    public ListView topScoreListView;
    public ArrayList<String> scoreList;
    private Button goHome;
    private ScoreDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_scores);

//        topScoreListView = findViewById(R.id.scoresListView);
        dbHelper=new ScoreDatabaseHelper(topScores.this);

        goHome=(Button) findViewById(R.id.go_home);
       goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent top=new Intent(topScores.this,GameActivity.class);
                startActivity(top);
                finish();
            }
        });
        scoreList = dbHelper.getAllScores();

        TextView scoresTextView = findViewById(R.id.scores_text_view);
        StringBuilder sb = new StringBuilder();
        int mn=Integer.min(scoreList.size(),10);
        for (int i = 0; i < mn; i++) {
            int position = i + 1;
            int score = Integer.parseInt(scoreList.get(i));
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
                    sb.append("th:    Score ").append(score).append("\n");
                    break;
            }
        }
        scoresTextView.setText(sb.toString());

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item,R.id.text_view,scoreList.subList(0, Math.min(scoreList.size(), 10)));
//        topScoreListView.setAdapter(adapter);
    }
}