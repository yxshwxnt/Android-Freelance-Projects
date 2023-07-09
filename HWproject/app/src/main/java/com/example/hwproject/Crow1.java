package com.example.hwproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Crow1 {
    Bitmap crow[]=new Bitmap[9];
    int crowX,crowY,velocity,crowFrame;
    Random random;

    public Crow1(Context context) {
        crow[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.plane1);
        crow[1]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane2);
        crow[2]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane3);
        crow[3]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane4);
        crow[4]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane5);
        crow[5]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane6);
        crow[6]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane7);
        crow[7]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane8);
        crow[8]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane9);
        random=new Random();
        crowFrame=0;
        resetPosition();
    }
    public Bitmap getBitmap(){
        return crow[crowFrame];
    }
    public int getWidth(){
        return  crow[0].getWidth();
    }
    public int getHeight(){
        return crow[0].getHeight();
    }
    public void resetPosition(){
        crowX=GameView.dwidth+random.nextInt(1200);
        crowY=random.nextInt(300);
        velocity=10+random.nextInt(17);
    }
}
