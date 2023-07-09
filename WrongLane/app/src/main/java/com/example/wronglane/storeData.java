package com.example.wronglane;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class storeData extends SQLiteOpenHelper {
    private static final String databse = "score.db";
    private static final int version = 1;
    public static final String SCORE = "score";
    public static final String SCORES = "points_tb";
    private static final String CREATE_TABLE = "CREATE TABLE " + SCORES+ "("+ SCORE + " INTEGER UNIQUE"+ ")";

    public storeData(@Nullable Context context) {
        super(context,databse,null,version);
    }

    public void addData(int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCORE, score);
        db.insert(SCORES, null, values);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + SCORES);
        onCreate(database);
    }
    public ArrayList<String> getData(){
        ArrayList<String> scores=new ArrayList<String>();
        String query = "SELECT * FROM " + SCORES + " ORDER BY " + SCORE + " DESC LIMIT 10";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int point=cursor.getInt(cursor.getColumnIndex(SCORE));
                scores.add(String.valueOf(point));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return scores;
    }
}
