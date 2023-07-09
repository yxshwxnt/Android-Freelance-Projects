package com.example.wronglane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OppositeCars {
    Context context;
    Bitmap cars;
    Bitmap car2[]=new Bitmap[3];
    Random rnd=new Random();
    private static final List<Integer> S = new ArrayList<>(3);
    int carFrame=0;
    int car2X,car2Y;
    int carVelocity;
    Random random;
    int carPos[]=new int[3];

    public OppositeCars(Context context){
        this.context = context;
        cars = BitmapFactory.decodeResource(context.getResources(), R.drawable.carw);
        carPos[0]=240;
        carPos[1]=450;
        carPos[2]=560;
        car2[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.carw);
        car2[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.carw);
        car2[2]= BitmapFactory.decodeResource(context.getResources(),R.drawable.carw);
        random=new Random();
        resetPosition();
        random = new Random();
    }
    public Bitmap getCar2(int spikeFrame){
        return car2[carFrame];
    }
    public int getCarW(){
        return car2[0].getWidth();
    }
    public int getSpikeHeight(){
        return car2[0].getHeight();
    }
    public void resetPosition(){
        car2X=random.nextInt(GameView.dWidth-getCarW());
        int rnd=random.nextInt(3);
        car2Y=-200+random.nextInt(600)*-1;
        carVelocity=5+random.nextInt(4);
    }
}

