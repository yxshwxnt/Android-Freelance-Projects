package com.example.hwproject;

import static android.content.Context.VIBRATOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import android.os.Vibrator;
import android.widget.Toast;

public class GameView extends View {
    Bitmap background;
    Rect rect;
    private Paint scoreB=new Paint();
    private Paint lifeB=new Paint();
    static int dwidth, dheight;
    boolean touched=false;
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILIES = 30;
    ArrayList<Crow1> crow1;
    ArrayList<Crow1> crow2;
    Bitmap bullet, tank;
    float bulletX, bulletY;
    float sX, sY, fX, fY, dX, dY;
    float tempX, tempY;
    Paint borderPaint;
    int score=0;
    int life=10;
    Context context;
    Toast t;
    private MediaPlayer gameOver,c2Hit,fire;
    private Vibrator vibrator;
    public GameView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dwidth = size.x;
        dheight = size.y;
        rect = new Rect(0, 0, dwidth, dheight);
        handler = new Handler();

        scoreB.setColor(Color.BLACK);
        scoreB.setTextSize(70);
        scoreB.setTypeface(Typeface.DEFAULT);
        scoreB.setAntiAlias(true);

        lifeB.setColor(Color.BLUE);
        lifeB.setTextSize(70);
        lifeB.setTypeface(Typeface.DEFAULT);
        lifeB.setAntiAlias(true);
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        crow1 = new ArrayList<>();
        crow2 = new ArrayList<>();
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        c2Hit = MediaPlayer.create(context, R.raw.shot);
        fire=MediaPlayer.create(context,R.raw.fire);
        gameOver=MediaPlayer.create(context,R.raw.gameover);
        for (int i = 0; i < 2; i++) {
            Crow1 crow_1 = new Crow1(context);
            crow1.add(crow_1);
            Crow2 crow_2 = new Crow2(context);
            crow2.add(crow_2);
        }
        bullet = BitmapFactory.decodeResource(getResources(), R.drawable.cann_ball);
        tank = BitmapFactory.decodeResource(getResources(), R.drawable.tank);
        bulletX = bulletY = 0;
        sX = sY = fX = fY = dX = dY = 0;
        tempX = tempY = 0;
        borderPaint=new Paint();
        borderPaint.setColor(Color.BLUE);
        borderPaint.setStrokeWidth(5);
        this.context=context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        t=Toast.makeText(context,"Invalid Area: ",Toast.LENGTH_SHORT);
        super.onDraw(canvas);

        canvas.drawBitmap(background, null, rect, null);
        for (int i = 0; i < crow1.size(); i++) {
            canvas.drawBitmap(crow1.get(i).getBitmap(), crow1.get(i).crowX, crow1.get(i).crowY, null);
            crow1.get(i).crowFrame++;
            if (crow1.get(i).crowFrame > 8) {
                crow1.get(i).crowFrame = 0;
            }
            crow1.get(i).crowX -= crow1.get(i).velocity;
            if (crow1.get(i).crowX < -crow1.get(i).getWidth()) {
                crow1.get(i).resetPosition();
                life--;
                if(life==0){
                    Intent gameOver=new Intent(getContext(),GameOver.class);
                    gameOver.putExtra("score",score);
                    getContext().startActivity(gameOver);
                    ((Activity) context).finish();
                    destroyDrawingCache();
                }
            }
            // Crow 2 Start
            canvas.drawBitmap(crow2.get(i).getBitmap(), crow2.get(i).crowX, crow2.get(i).crowY, null);
            crow2.get(i).crowFrame++;
            if (crow2.get(i).crowFrame > 8) {
                crow2.get(i).crowFrame = 0;
            }
            crow2.get(i).crowX -= crow2.get(i).velocity;
            if (crow2.get(i).crowX < -crow2.get(i).getWidth()) {
                crow2.get(i).resetPosition();
                life--;
                if(life==0){
                    vibrateOnGameOver();
                    if(gameOver!=null && !gameOver.isPlaying()){
                        gameOver.start();
                    }
                    Intent gameOver=new Intent(getContext(),GameOver.class);
                    gameOver.putExtra("score",score);
                    getContext().startActivity(gameOver);
                    ((Activity) context).finish();
                    destroyDrawingCache();
                }
            }
            if((bulletX<=(crow1.get(i).crowX + crow1.get(i).getWidth())
                    && bulletX + bullet.getWidth()>= crow1.get(i).crowX
                    && bulletY<=(crow1.get(i).crowY + crow1.get(i).getHeight())
                    && bulletY>=crow1.get(i).crowY) && (fY>dheight*.75f)){
                crow1.get(i).resetPosition();
                score=score+1;
                vibrateOnHit();
                cHitSound();
            }
            if(bulletY>=230 && bulletY<=330){
                fireSound();
            }
            if(tempY>=230 && tempY<=330){
                fireSound();
            }
            if((bulletX<=(crow2.get(i).crowX + crow2.get(i).getWidth())
                    && bulletX + bullet.getWidth()>= crow2.get(i).crowX
                    && bulletY<=(crow2.get(i).crowY + crow2.get(i).getHeight())
                    && bulletY>=crow2.get(i).crowY) && (fY>dheight*.75f)){
                crow2.get(i).resetPosition();
                score=score+1;
                vibrateOnHit();
                cHitSound();
            }
        }
        if(sX==0 && sY== 0 && fX== 0 && fY==0){
            canvas.drawBitmap(tank,400,1717,null);
        }
        if(((Math.abs(fX - sX) > 0 || Math.abs(fY - sY) > 0) && fX>0 && fY>0) && fY>dheight*.75f) {
            canvas.drawBitmap(tank, fX - tank.getWidth() / 2, fY - tank.getHeight() / 2, null);
            if(t!=null){
                t.cancel();
            }
        }
        if(((Math.abs(fX - sX) > 0 || Math.abs(fY - sY) > 0) && fX>0 && fY>0) && fY<dheight*.75f) {
            vibrateOnHit();
            Toast t=Toast.makeText(context,"Wrong Input",Toast.LENGTH_SHORT);
            t.show();
            canvas.drawBitmap(tank,400,1717,null);
            if(t!=null){
                t.cancel();
            }
        }
        if(t!=null){
            t.cancel();
        }
        if ((Math.abs(dX) > 10 || Math.abs(dY) > 10)  && (sY>dheight*.75f)){
            bulletX = fX - bullet.getWidth() / 2 - tempX;
            bulletY = fY - bullet.getHeight() / 2 - tempY;
            canvas.drawBitmap(bullet, bulletX, bulletY, null);
            if(touched) {
//                fireSound();
            }
            tempX += dX;
            tempY += dY;
        }
        canvas.drawLine(0,dheight*0.75f,dwidth,dheight*.75f,borderPaint);
        canvas.drawText("Score: "+score,725,60,scoreB);
        canvas.drawText("Life: "+life,20,60,lifeB);
        handler.postDelayed(runnable, UPDATE_MILIES);
    }
    private void vibrateOnGameOver() {
        if (vibrator != null) {
            long[] pattern = {0, 100, 100, 100, 1000}; // vibration pattern
            vibrator.vibrate(pattern, -1); // -1 to vibrate only once
        }
    }

    private void vibrateOnHit() {
        if (vibrator != null) {
            vibrator.vibrate(125); // 100 milliseconds vibration
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_VOLUME_UP)){
//            sX=550;
//            sY=1713;
            Log.d( "onKeyUp: ","pressed");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX=dY=fX=fY=tempX=tempY=0;
                sX = event.getX();
                sY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                fX = event.getX();
                fY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                fX = event.getX();
                fY = event.getY();
                bulletX = event.getX();
                bulletY = event.getY();
                dX = fX - sX;
                dY = fY - sY;
                break;
        }
        return true;
    }
    private void cHitSound() {
        if (c2Hit != null && !c2Hit.isPlaying()) {
            c2Hit.start();
        }
    }
    private void fireSound() {
        if (fire != null && !fire.isPlaying()) {
            fire.start();
        }
    }
}