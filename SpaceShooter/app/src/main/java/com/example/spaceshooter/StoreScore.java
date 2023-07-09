package com.example.spaceshooter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StoreScore extends SQLiteOpenHelper {
    private static final String databse = "db.db";
    private static final int version = 1;
    public static final String SCORE = "score";
    public static final String SCORES = "points_tb";
    private static final String CREATE_TABLE = "CREATE TABLE " + SCORES+ "("+ SCORE + " INTEGER UNIQUE"+ ")";

    public StoreScore(@Nullable Context context) {
        super(context,databse,null,version);
    }

    public void addScore(int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCORE, score);
        db.insert(SCORES, null, values);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + SCORES);
        onCreate(db);
    }

    public ArrayList<String> scoreArr(){
        ArrayList<String> scoreArr=new ArrayList<String>();
        String q = "SELECT * FROM " + SCORES + " ORDER BY " + SCORE + " DESC LIMIT 10";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int point=cursor.getInt(cursor.getColumnIndex(SCORE));
                scoreArr.add(String.valueOf(point));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scoreArr;
    }
}
