package com.example.wronglane;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View implements SensorEventListener {
    Bitmap car,bg;
    private SensorManager sensorManager;
    private MediaPlayer crash;
    private Sensor accelerometer;
    public long lastTime=0;
    boolean gameOver=false;
    Rect rectBg;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS=10;
    private MediaPlayer hitSound;
    Runnable runnable;
    Paint textPaint=new Paint();
    Paint healthPaint=new Paint();
    int health=5;
    int score=0;
    static int dWidth,dHeight;
    Random random;
    float carX,carY;
    float oldX=0;
    ArrayList<OppositeCars> oppCars;

    public GameView(Context context) {
        super(context);
        this.context=context;
        bg= BitmapFactory.decodeResource(getResources(),R.drawable.road);
        car=BitmapFactory.decodeResource(getResources(),R.drawable.carred);
        Display display=((Activity) getContext()).getWindowManager().getDefaultDisplay();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        crash=MediaPlayer.create(context,R.raw.crash);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Point size=new Point();
        display.getSize(size);
        dWidth=size.x;
        dHeight=size.y;
        rectBg=new Rect(0,0,dWidth,dHeight);
        handler = new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextSize(80);
        textPaint.setTextAlign(Paint.Align.LEFT);
        healthPaint.setColor(Color.RED);
        healthPaint.setTextSize(80);
        healthPaint.setTextAlign(Paint.Align.RIGHT);
        random=new Random();
        carX=dWidth/2 - car.getWidth()/2;
        carY=dHeight - 5 - car.getHeight();
        oppCars=new ArrayList<>();
        for(int i=0;i<3;i++){
            OppositeCars oppCar=new OppositeCars(context);
            oppCars.add(oppCar);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bg,null,rectBg,null);
        canvas.drawBitmap(car,carX,carY,null);
        for(int i=0;i<oppCars.size();i++){
            canvas.drawBitmap(oppCars.get(i).getCar2(oppCars.get(i).carFrame),oppCars.get(i).car2X,oppCars.get(i).car2Y,null);
            oppCars.get(i).carFrame++;
            if(oppCars.get(i).carFrame>2){
                oppCars.get(i).carFrame=0;
            }
            oppCars.get(i).car2Y+= oppCars.get(i).carVelocity;
            if(oppCars.get(i).car2Y + oppCars.get(i).getSpikeHeight()>=dHeight){
                score+=10;
                oppCars.get(i).resetPosition();
            }
        }
        for(int i=0;i<oppCars.size();i++){
            if(oppCars.get(i).car2X + oppCars.get(i).getCarW() >= carX &&
                    oppCars.get(i).car2X<=carX+car.getWidth()
                    && oppCars.get(i).car2Y + oppCars.get(i).getCarW()>=carY
                    && oppCars.get(i).car2Y + oppCars.get(i).getCarW()<=carY + car.getHeight()){
                health--;
                if(crash!=null){
                    crash.start();
                }
                oppCars.get(i).resetPosition();
                if(health==0){
                    gameEnd();
                }
            }
        }
        canvas.drawText("Score:  "+score,20,120,textPaint);
        canvas.drawText("Health: "+health,dWidth-20,120,healthPaint);
        if(health==0){
            gameOver=true;
            sensorManager.unregisterListener(this);
        }
        if(!gameOver){
            handler.postDelayed(runnable,UPDATE_MILLIS);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long currTime=System.currentTimeMillis();
        long timeDiff=currTime-lastTime;
        lastTime=currTime;
        float x=sensorEvent.values[1];
        float shift=(oldX-x)*timeDiff*3.15f;
        carX+=shift;
        if (carX < 0) {
            carX = 0;
        } else if (carX >= dWidth - car.getWidth()) {
            carX = dWidth - car.getWidth();
        }
        oldX=x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void gameEnd(){
        handler=null;
        Intent end=new Intent(context,GameEnd.class);
        end.putExtra("score",score);
        context.startActivity(end);
        ((Activity) context).finish();
    }
}
