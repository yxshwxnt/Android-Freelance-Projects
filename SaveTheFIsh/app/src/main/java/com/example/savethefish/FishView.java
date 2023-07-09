package com.example.savethefish;

import static android.content.Context.VIBRATOR_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class FishView extends View {
    private Bitmap fish[]=new Bitmap[2];
    private Bitmap backgroundImage;
    private Paint scorePaint=new Paint();
    private Bitmap life[]= new Bitmap[2];
    private int fishX=10;
    private int fishY;
    private static int fishSpeed;
    private int cWidth,cHeight;
    private boolean touch;
    private int foodX,foodY,foodSpeed=16;
    private Paint foodPaint=new Paint();
    private int mineX,mineY,mineSpeed=22;
    private static MediaPlayer tap;
    private MediaPlayer coin;
    private MediaPlayer bomb;
    private Bitmap mine;
    private Vibrator vibrator;

    private int score,lifeCnt;
    public FishView(Context context) {
        super(context);

        fish[0]= BitmapFactory.decodeResource(getResources(),R.drawable.fish1);
        fish[1]= BitmapFactory.decodeResource(getResources(),R.drawable.fish2);
        
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        tap = MediaPlayer.create(context, R.raw.tap);
        coin = MediaPlayer.create(context, R.raw.coin);
        bomb=MediaPlayer.create(context,R.raw.bomb);


        backgroundImage=BitmapFactory.decodeResource((getResources()),R.drawable.bg);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT);
        scorePaint.setAntiAlias(true);

        foodPaint.setColor(Color.RED);
        foodPaint.setAntiAlias(false);

        mine=BitmapFactory.decodeResource(getResources(),R.drawable.mine);

        life[0]=BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        life[1]=BitmapFactory.decodeResource(getResources(),R.drawable.gheart);

        fishY=550;
        score=0;
        lifeCnt=3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cWidth=canvas.getWidth();
        cHeight=canvas.getHeight();

        canvas.drawBitmap(backgroundImage,0,0,null);


        int minFishY=fish[0].getHeight();
        int maxFishY=cHeight-fish[0].getHeight()*3;
        fishY=fishY+fishSpeed;
        if(fishY<minFishY){
            fishY=minFishY;
        }
        if(fishY>maxFishY){
            fishY=maxFishY;
        }
        fishSpeed=fishSpeed+2;
        if(touch){
            canvas.drawBitmap(fish[1],fishX,fishY,null);
            touch=false;
        }
        else{
            canvas.drawBitmap(fish[0],fishX,fishY,null);
        }
        //food
        foodX=foodX-foodSpeed;
        if(hitCheck(foodX,foodY)){
            score=score+1;
            if (coin != null && !coin.isPlaying()) {
                coin.start();
            }
            foodX=-100;
        }
        if(foodX<0){
            foodX=cWidth+21;
            foodY=(int)Math.floor(Math.random()*(maxFishY-minFishY)+minFishY);
        }
        canvas.drawCircle(foodX,foodY,20,foodPaint);
        //danger
        mineX=mineX-mineSpeed;
        if(hitCheck(mineX,mineY)){
            mineX=-100;
            lifeCnt=lifeCnt-1;
            if (bomb != null && !bomb.isPlaying()) {
                bomb.start();
            }
            vibrateOnHit();
            if(lifeCnt==0){
                vibrateOnGameOver();
                Toast.makeText(getContext(),"Game Over",Toast.LENGTH_SHORT).show();
                Intent gameOver=new Intent(getContext(),GameOver.class);
                gameOver.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOver.putExtra("score",score);
                getContext().startActivity(gameOver);
            }
        }
        if(mineX<0){
            mineX=cWidth+21;
            mineY=(int)Math.floor(Math.random()*(maxFishY-minFishY)+minFishY);
        }
        canvas.drawBitmap(mine,mineX,mineY,null);
        for(int i=0;i<3;i++){
            int x=(int)(580+life[0].getWidth()*1.5*i);
            int y=30;
            if(i<lifeCnt){
                canvas.drawBitmap(life[0],x,y,null);
            }
            else {
                canvas.drawBitmap(life[1],x,y,null);
            }
        }
        //score
        canvas.drawText("score: "+score,20,60,scorePaint);
    }


    public boolean hitCheck(int x,int y){
        if(fishX<x && x<(fishX+fish[0].getWidth()) && fishY<y && y<(fishY+fish[0].getHeight())){
            return true;
        }
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Log.d("onKeyDown: ","JUmp");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void vibrateOnGameOver() {
        if (vibrator != null) {
            long[] pattern = {0, 100, 100, 100, 1000}; // vibration pattern
            vibrator.vibrate(pattern, -1); // -1 to vibrate only once
        }
    }
    public static void jump(){
        fishSpeed=-22;
        if (tap != null && !tap.isPlaying()) {
            tap.start();
        }
    }
    private void vibrateOnHit() {
        if (vibrator != null) {
            vibrator.vibrate(125); // 100 milliseconds vibration
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            touch=true;
            fishSpeed=-22;
            if (tap != null && !tap.isPlaying()) {
                tap.start();
            }
        }
        return true;
    }
}
