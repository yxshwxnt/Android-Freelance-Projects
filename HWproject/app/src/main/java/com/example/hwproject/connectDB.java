package com.example.hwproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class connectDB extends SQLiteOpenHelper {

    private static final String db_name = "topScore.db";
    private static final int db_ver = 1;
    public static final String SCORE = "score";
    public static final String table1 = "scores";
    private static final String create_tb = "CREATE TABLE " + table1+ "("+ SCORE+" INTEGER UNIQUE"+ ")";

    public connectDB(Context context) {
        super(context, db_name, null, db_ver);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_tb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldDB, int newDB) {
        db.execSQL("DROP TABLE IF EXISTS " + table1);
        onCreate(db);
    }

    public void appendScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCORE, score);
        Log.d("addScore: ", String.valueOf(score));
        db.insert(table1, null, values);
        db.close();
    }

    public ArrayList<String> getTop10() {
        ArrayList<String> top10list=new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + table1 + " ORDER BY " + SCORE + " DESC LIMIT 10";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int _score=cursor.getInt(cursor.getColumnIndex(SCORE));
                top10list.add(String.valueOf(_score));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return top10list;
    }
}
