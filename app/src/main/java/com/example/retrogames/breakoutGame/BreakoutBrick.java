package com.example.retrogames.breakoutGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.GameObject;

public class BreakoutBrick implements GameObject
{
    private double positionX;
    private double positionY;

    private double length;
    private double height;
    private Paint paint;

    public BreakoutBrick(Context context, double positionX, double positionY, double length, double height) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.length = length;
        this.height = height;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect((float) positionX, (float) positionY, (float) (positionX + length), (float) (positionY + height), paint);
    }
    @Override
    public double getPositionX() { return positionX; }
    @Override
    public double getPositionY() { return positionY; }
    @Override
    public double getHeight() { return height; }
    @Override
    public double getLength() { return length; }

    public double getVelocityX() { return 0; }


}
