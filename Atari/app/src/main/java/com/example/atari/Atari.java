package com.example.atari;

import android.graphics.Paint;

public class Atari {
    private boolean isVisible;
    public int row,col,width,height;
    Paint paint=new Paint();
    public Atari(int row, int col, int width, int height,Paint p){
        isVisible=true;
        this.row=row;
        this.col=col;
        this.width=width;
        this.height=height;
        this.paint=p;
    }
    public void setInvisible(){
        isVisible=false;
    }

    public boolean getVisibilty() {
        return isVisible;
    }
}
