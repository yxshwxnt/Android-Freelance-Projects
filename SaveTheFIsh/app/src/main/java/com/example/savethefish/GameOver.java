package com.example.savethefish;

import static com.example.savethefish.ScoreDatabaseHelper.KEY_SCORE;
import static com.example.savethefish.ScoreDatabaseHelper.TABLE_SCORES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;

public class GameOver extends AppCompatActivity {
    private Button startAgain,top_scores,exitApp;
    private TextView showScore;
    private String score;
    public ScoreDatabaseHelper dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        dbHandler=new ScoreDatabaseHelper(GameOver.this);


        exitApp=(Button) findViewById(R.id.exit_button);
        startAgain=(Button) findViewById(R.id.playAgain);
        showScore=(TextView) findViewById(R.id.showScore);
        score=getIntent().getExtras().get("score").toString();
        top_scores = (Button) findViewById(R.id.showScoresButton);
        startAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(GameOver.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        top_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent top=new Intent(GameOver.this,topScores.class);
                startActivity(top);
                finish();
            }
        });
        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
        showScore.setText("Current Game Score: "+score);
        dbHandler.addScore(Integer.parseInt(score));
    }

}