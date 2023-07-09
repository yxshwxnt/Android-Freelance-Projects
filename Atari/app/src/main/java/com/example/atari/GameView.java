package com.example.atari;

import android.app.Activity;
import android.content.AsyncQueryHandler;
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
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Random;

public class GameView extends View implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdateTime = 0;
    public MediaPlayer atariSound, paddleSound;
    float ballX,ballY;
    Context context;
    Speed speed=new Speed(25,32);
    Handler handler;
    final long UPDATE_MILLIS=30;
    Runnable runnable;
    Paint textP=new Paint();
    Paint healthP=new Paint();
    Paint atariP= new Paint();
    Paint[] colors = new Paint[4];
    float TEXT_SIZE=120;
    float surfaceX,surfaceY;
    float oldX,oldSurfaceX;
    float lastX=0;
    public int points=0;
    int life=3;
    Bitmap ball,surface,heart,yHeart,gHeart;
    int dispW,dispH;
    int ballW,ballH;
    Random random;
    Atari[] ataris=new Atari[3000000];
    int totNum=0;
    int burstedAtari=0;
    int cnt=1;
    boolean gameOver=false;

    public GameView(Context context) {
        super(context);
        this.context=context;
        ball= BitmapFactory.decodeResource(getResources(),R.drawable.ball);
        surface=BitmapFactory.decodeResource(getResources(),R.drawable.surfcace);
        heart=BitmapFactory.decodeResource(getResources(),R.drawable.heart);
        yHeart=BitmapFactory.decodeResource(getResources(),R.drawable.yheart);
        gHeart=BitmapFactory.decodeResource(getResources(),R.drawable.gheart);
        atariSound = MediaPlayer.create(context, R.raw.atari);
        paddleSound=MediaPlayer.create(context,R.raw.paddle);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                 invalidate();
            }
        };

        // Get a reference to the system's SensorManager
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Get a reference to the accelerometer sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register this activity as a listener for the accelerometer sensor
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        textP.setColor(Color.RED);
        textP.setTextSize(TEXT_SIZE);
        textP.setTextAlign(Paint.Align.LEFT);
        healthP.setColor(Color.GREEN);
        atariP.setColor((Color.argb(255,249,129,0)));
        Display display=((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        dispW=size.x;
        dispH=size.y;
        random=new Random();
        ballX=random.nextInt(dispW-50);
        ballY=dispH/3;
        surfaceY=(dispH*4)/5;
        surfaceX=dispW/2 - surface.getWidth()/2;
        ballW=ball.getWidth();
        ballH=ball.getHeight();
        formLayout();
    }
    private void formLayout(){
        int bWidth=dispW/8;
        int bHeight=dispH/16;
        for(int j=0;j<8;j++){
            for(int i=0;i<3;i++){
                ataris[totNum]=new Atari(i,j,bWidth,bHeight,colors[2]);
                totNum+=1;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        ballX+=speed.getX();
        ballY+=speed.getY();
        if((ballX>=dispW - ball.getWidth()) || ballX<=0){
            speed.setX(speed.getX()*-1);
        }
        if(ballY<=0){
            speed.setY(speed.getY()*-1);
        }
        if(ballY>surfaceY+surface.getHeight()){
            ballX=1+random.nextInt(dispW-ball.getWidth()-1);
            ballY=dispH/3;
            speed.setX((int) (xSpeed()+(0.5*cnt)));
            speed.setY((int) (32+(0.5*cnt)));
            life--;
            if(life==0){
                gameOver=true;
                sensorManager.unregisterListener(this);
                gameOverStart();
            }
        }
        if(((ballX+ball.getWidth())>=surfaceX)
            && (ballX<=surfaceX+surface.getWidth())
                && (ballY+ball.getHeight()>=surfaceY)
                && (ballY+ball.getHeight()<=surfaceY+surface.getHeight())){
                speed.setX(speed.getX()+1);
                speed.setY((speed.getY()+1)*-1);
            Log.d("onDraw: ","here");
                if (paddleSound != null && !paddleSound.isPlaying()) {
                    paddleSound.start();
                }
        }
        canvas.drawBitmap(ball,ballX,ballY,null);
        canvas.drawBitmap(surface, surfaceX, surfaceY, null);
        for(int i=0;i<totNum;i++){
            if(ataris[i].getVisibilty()){
                canvas.drawRect(ataris[i].col*ataris[i].width+1,ataris[i].row*ataris[i].height+1,ataris[i].col*ataris[i].width+ataris[i].width-1,ataris[i].row*ataris[i].height+ataris[i].height-1,atariP);
            }
        }
        canvas.drawText(""+points,20,TEXT_SIZE,textP);
        if(life==2){
            healthP.setColor(Color.YELLOW);
        } else if (life==1) {
            healthP.setColor(Color.RED);
        }
        canvas.drawRect(dispW-200,30,dispH-200 + 60*life,80,healthP);
        for(int i=0;i<totNum;i++){
            if(ataris[i].getVisibilty()){
                if(ballX+ballW>=ataris[i].col * ataris[i].width
                && ballX<=ataris[i].col*ataris[i].width+ataris[i].width
                && ballY<=ataris[i].row*ataris[i].height+ataris[i].height
                && ballY>=ataris[i].row*ataris[i].height){
                    speed.setY((speed.getY()+1)*-1);
                    ataris[i].setInvisible();
                    points+=10;
                    burstedAtari+=1;
                    if (atariSound != null && !atariSound.isPlaying()) {
                        atariSound.start();
                    }
                    if(burstedAtari%24==0){
//                        gameOverStart();
                        cnt++;
                        formLayout();
                    }
                }
            }
        }
        if(burstedAtari==totNum){
            gameOver=true;
        }
        if(!gameOver){
            handler.postDelayed(runnable,UPDATE_MILLIS);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float touchX=event.getX();
//        float touchY=event.getY();
//        if(touchY>=surfaceY){
//            int action=event.getAction();
//            if(action==MotionEvent.ACTION_DOWN){
//                oldX=event.getX();
//                oldSurfaceX=surfaceX;
//            }
//            if(action==MotionEvent.ACTION_MOVE){
//                float shift=oldX-touchX;
//                float newSurface=oldSurfaceX-shift;
//                if(newSurface<0){
//                    surfaceX=0;
//                } else if (newSurface>=dispW-surface.getWidth()) {
//                    surfaceX=dispW-surface.getWidth();
//                }
//                else{
//                    surfaceX=newSurface;
//                }
//            }
//        }
//        return true;
//    }
    private void gameOverStart(){
        handler.removeCallbacksAndMessages(null);
        Toast.makeText(getContext(),"Game Over: ",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(context,GameEnd.class);
        intent.putExtra("Points",points);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
    private int xSpeed(){
        int[] vals={-35,-30,-25,-20,20,30,35};
        int ind=random.nextInt(6);
        return vals[ind];
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get the current time and the change in time since the last update
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        // Get the acceleration values from the event
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
//        float shift=oldX-x;
//        Log.d("onSensorChanged: ", String.valueOf(shift));
//        float newSurface=oldSurfaceX-shift;
//        if(newSurface<0){
//            surfaceX=0;
//        } else if (newSurface>=dispW-surface.getWidth()) {
//            surfaceX=dispW-surface.getWidth();
//        }
//        else{
//            surfaceX=newSurface;
//        }
        // Calculate the change in position of the paddle based on the change in acceleration
//        float deltaX = (x-lastX) * deltaTime * 0.1f;
        float shift=(oldX-x)*deltaTime*3.15f;
        Log.d("onSensorChanged: ", String.valueOf(shift));
        surfaceX += shift;
        // Keep the paddle within the screen boundaries
        if (surfaceX < 0) {
            surfaceX = 0;
        } else if (surfaceX > dispW - surface.getWidth()) {
            surfaceX = dispW - surface.getWidth();
        }
        oldX=x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Do Nothing
    }
}
