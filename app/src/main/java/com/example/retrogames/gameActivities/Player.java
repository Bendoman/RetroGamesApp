package com.example.retrogames.gameActivities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

public class Player {
    private double positionX;
    private double positionY;
    private double length;
    private double height;
    private Paint paint;

    private double canvasWidth = 0;
    private double canvasHeight = 0;

    public Player(Context context, double positionX, double positionY, double length, double height) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.length = length;
        this.height = height;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    public double getLength() {
        return length;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect((float) positionX, (float) positionY, (float) (positionX + length), (float) (positionY + height), paint);
        if(canvasWidth == 0) {
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
        }
    }

    public void update() {
    }

    public void setPosition(double positionX) {
            this.positionX  = positionX;
    }
    public void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
