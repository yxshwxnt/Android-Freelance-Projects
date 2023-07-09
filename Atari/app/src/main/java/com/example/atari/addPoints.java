package com.example.atari;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class addPoints extends SQLiteOpenHelper {
    private static final String DB_NAME = "points_db.db";
    private static final int DB_VER = 1;
    public static final String POINTS = "points";   //ATTr.
    public static final String LEADERBOARD = "points_tb";   //table
    private static final String Crate_query = "CREATE TABLE " + LEADERBOARD
            + "("
            + POINTS + " INTEGER UNIQUE"
            + ")";

    public addPoints(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }
    public void addPts(int point) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POINTS, point);
        db.insert(LEADERBOARD, null, values);
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Crate_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LEADERBOARD);
        onCreate(db);
    }
    public ArrayList<String> ptsList() {
        ArrayList<String> listPoints=new ArrayList<String>();
        String q = "SELECT * FROM " + LEADERBOARD + " ORDER BY " + POINTS + " DESC LIMIT 10";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int point=cursor.getInt(cursor.getColumnIndex(POINTS));
                listPoints.add(String.valueOf(point));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listPoints;
    }
}
