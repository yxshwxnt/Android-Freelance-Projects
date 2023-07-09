package com.example.spaceshooter;

public class Score {
    private int score;
    private String date;
    public Score(int score) {
        this.score = score;
    }
    public Score(int score, String date) {
        this.score = score;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }
}
