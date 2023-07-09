package com.example.hwproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Crow2 extends Crow1{
    Bitmap[] crow=new Bitmap[9];
    public Crow2(Context context) {
        super(context);
        crow[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_1);
        crow[1]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_2);
        crow[2]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_3);
        crow[3]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_4);
        crow[4]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_5);
        crow[5]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_6);
        crow[6]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_7);
        crow[7]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_8);
        crow[8]=BitmapFactory.decodeResource(context.getResources(),R.drawable.plane_9);
    }

    @Override
    public Bitmap getBitmap() {
        return crow[crowFrame];
    }

    @Override
    public int getWidth() {
        return  crow[0].getWidth();
    }

    @Override
    public int getHeight() {
        return crow[0].getHeight();
    }

    @Override
    public void resetPosition() {
        crowX=GameView.dwidth+random.nextInt(1500);
        crowY=random.nextInt(400);
        velocity=10+random.nextInt(17);
    }
}
