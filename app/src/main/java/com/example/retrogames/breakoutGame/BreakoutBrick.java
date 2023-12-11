package com.example.retrogames.breakoutGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.GameObject;

/**
 * Game object for breakout.
 */
public class BreakoutBrick implements GameObject
{
    private final Paint paint;
    private final float length;
    private final float height;
    private final float positionX;
    private final float positionY;

    public BreakoutBrick(Context context, float positionX, float positionY, float length, float height) {
        this.length = length;
        this.height = height;
        this.positionX = positionX;
        this.positionY = positionY;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect( positionX,  positionY, (positionX + length),  (positionY + height), paint);
    }
    @Override
    public double getPositionX() { return positionX; }
    @Override
    public double getPositionY() { return positionY; }
    @Override
    public double getHeight() { return height; }
    @Override
    public double getLength() { return length; }

    // Needs to be implemented because of interface, always zero as these bricks don't move
    public double getVelocityX() { return 0; }
}
