package com.example.hwproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {
//        Log.i("ImageButton","Clicked");
        Intent intent=new Intent(this,StartGame.class);
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