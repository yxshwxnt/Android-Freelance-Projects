package com.example.atari;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Leaderboard extends AppCompatActivity {
    public ListView leaderB;
    public addPoints l;
    private ArrayList<String> ptsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        l=new addPoints(this);
        ptsList=l.ptsList();
        displaySortedPointsInListView(ptsList);
    }
    public void displaySortedPointsInListView(List<String> points) {
        ListView listView = findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, points);
        listView.setAdapter(adapter);
    }
}