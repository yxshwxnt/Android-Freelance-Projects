package com.example.savethefish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.SharedPreferences;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private Button top_scores,exitApp;
    private ImageButton startB;
    public SharedPreferences MyPrefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exitApp=(Button) findViewById(R.id.exit_button);
        top_scores = (Button) findViewById(R.id.showScoresButton);
        startB= (ImageButton) findViewById(R.id.start_b);

        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread=new Thread(){
                    @Override
                    public void run() {
                        try{
                            sleep(500);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        finally {
                            Intent mainIntent=new Intent(GameActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                        }
                    }
                };
                thread.start();
            }
        });
        top_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent top=new Intent(GameActivity.this,topScores.class);
                startActivity(top);
            }
        });
        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
