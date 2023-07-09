package com.example.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Spike {
    Context context;
    Bitmap spike[]=new Bitmap[3];
    int spikeFrame=0;
    static int sx,sy;
    int spikeX;
    int spikeY;
    int spikeVelocity;
    Random random;

    public Spike(Context context){
        this.context=context;
        spike[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
        spike[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
        spike[2]= BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
        random=new Random();
        resetPosition();
    } 
    public Bitmap getSpike(int spikeFrame){
        return spike[spikeFrame];
    }
    public int getSpikeWidth(){
        return spike[0].getWidth();
    }
    public int getSpikeHeight(){
        return spike[0].getHeight();
    }
    public void resetPosition(){
        spikeX=random.nextInt(GameView.dWidth-getSpikeWidth());
        spikeY=-200+random.nextInt(600)*-1;
//        spikeVelocity=14+random.nextInt(8);
        spikeVelocity=5+random.nextInt(4);
        sx=random.nextInt(GameView.dWidth-getSpikeWidth());
        sy=-200+random.nextInt(600)*-1;
    }
}
