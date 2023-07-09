package com.example.spaceshooter;

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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;


public class GameView extends View implements SensorEventListener {

    Bitmap background, ship,lifeImage;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    Vibrator vibrator;
    public long lastTime=0;
    boolean gameOver=false;
    Rect rectBg;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS=10;
    private MediaPlayer hitSound;
    Runnable runnable;
    Paint textPaint=new Paint();
    int life=3;
    int points=0;
    static int dWidth,dHeight;
    Random random;
    float shipX,shipY;
    float oldX;
    float oldShipX;
    ArrayList<Spike> spikes;
    Explosion explosion;
    ArrayList<Explosion> explosions;

    public GameView(Context context){
        super(context);
        this.context=context;
        background= BitmapFactory.decodeResource(getResources(),R.drawable.start);
        ship=BitmapFactory.decodeResource(getResources(),R.drawable.rocket1);
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        Display display=((Activity) getContext()).getWindowManager().getDefaultDisplay();

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        hitSound = MediaPlayer.create(context, R.raw.hit);
        explosions = new ArrayList<>();

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
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(120);
        textPaint.setTextAlign(Paint.Align.LEFT);
        random=new Random();
        shipX=dWidth/2 - ship.getWidth()/2;
        shipY=dHeight - 5 - ship.getHeight();
        spikes=new ArrayList<>();
        for(int i=0;i<3;i++){
            Spike spike=new Spike(context);
            spikes.add(spike);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background,null,rectBg,null);
        canvas.drawBitmap(ship,shipX,shipY,null);
        for(int i=life; i>=1; i--){
            canvas.drawBitmap(lifeImage, dWidth - lifeImage.getWidth() * i, 0, null);
        }
        for(int i=0;i<spikes.size();i++){
            canvas.drawBitmap(spikes.get(i).getSpike(spikes.get(i).spikeFrame),spikes.get(i).spikeX,spikes.get(i).spikeY,null);
            spikes.get(i).spikeFrame++;
            if(spikes.get(i).spikeFrame>2){
                spikes.get(i).spikeFrame=0;
            }
            spikes.get(i).spikeY+= spikes.get(i).spikeVelocity;
            if(spikes.get(i).spikeY + spikes.get(i).getSpikeHeight()>=dHeight-5){
                points+=10;
                spikes.get(i).resetPosition();
            }
        }
        for(int i=0;i<spikes.size();i++){
            if(spikes.get(i).spikeX + spikes.get(i).getSpikeWidth() >= shipX &&
            spikes.get(i).spikeX<=shipX+ship.getWidth()
            && spikes.get(i).spikeY + spikes.get(i).getSpikeWidth()>=shipY
            && spikes.get(i).spikeY + spikes.get(i).getSpikeWidth()<=shipY + ship.getHeight()){
                life--;
                playHitSound();
                vibrateOnHit();
//                explosion = new Explosion(context, Spike.spikeX,Spike.spikeY);
//                explosions.add(explosion);
                spikes.get(i).resetPosition();
                if(life==0){
                    handler = null;
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }
        canvas.drawText(""+points,20,120,textPaint);
        if(life==0){
            gameOver=true;
            sensorManager.unregisterListener(this);
        }
        for(int i=0; i < explosions.size(); i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).eX, explosions.get(i).eY, null);
            explosions.get(i).explosionFrame++;
            if(explosions.get(i).explosionFrame > 8){
                explosions.remove(i);
            }
        }
        if(!gameOver){
            handler.postDelayed(runnable,UPDATE_MILLIS);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY=event.getY();
        if(touchY>=shipY){
            int action=event.getAction();
            if(action==MotionEvent.ACTION_DOWN){
                oldX=event.getX();
                oldShipX=shipX;
            }
            if(action==MotionEvent.ACTION_MOVE){
                float shift=oldX-touchX;
                float newShipX=oldShipX-shift;
                if(newShipX<=0){
                    shipX=0; 
                } else if (newShipX>=dWidth-ship.getWidth()) {
                    shipX=dWidth-ship.getWidth();
                }
                else{
                    shipX=newShipX;
                }
            }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long currTime=System.currentTimeMillis();
        long timeDiff=currTime-lastTime;
        lastTime=currTime;

        float x=sensorEvent.values[0];
        float shift=(oldX-x)*timeDiff*2.2f;
        shipX-=shift;
        if (shipX < 0) {
            shipX = 0;
        } else if (shipX >= dWidth - ship.getWidth()) {
            shipX = dWidth - ship.getWidth();
        }
        oldX=x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void playHitSound() {
        if (hitSound != null && !hitSound.isPlaying()) {
            hitSound.start();
        }
    }
    private void vibrateOnHit() {
        if (vibrator != null) {
            vibrator.vibrate(100); // 100 milliseconds vibration
        }
    }
}
