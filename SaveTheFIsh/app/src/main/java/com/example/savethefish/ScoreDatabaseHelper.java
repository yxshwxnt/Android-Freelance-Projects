package com.example.savethefish;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.material.color.utilities.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoreDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "score_database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String KEY_SCORE = "score";
    public static final String TABLE_SCORES = "scores";
    private static final String CREATE_TABLE_SCORES = "CREATE TABLE " + TABLE_SCORES
            + "("
            + KEY_SCORE + " INTEGER UNIQUE"
            + ")";

    public ScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SCORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public void addScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, score);
        Log.d("addScore: ", String.valueOf(score));
        db.insert(TABLE_SCORES, null, values);
        db.close();
    }

    public ArrayList<String> getAllScores() {
        ArrayList<String> scoreList=new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_SCORES + " ORDER BY " + KEY_SCORE + " DESC LIMIT 10";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int _score=cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
                scoreList.add(String.valueOf(_score));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scoreList;
    }
}
