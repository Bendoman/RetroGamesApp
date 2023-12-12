package com.example.retrogames.tilterGame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Defines the area in which the tilter ball can be drawn without ending the game
 */
public class TilterPlayingField
{
    public Paint paint;
    public int positionX;
    public int positionY;
    public int playingFieldWidth;
    public int playingFieldHeight;

    public TilterPlayingField(Canvas canvas)
    {
        playingFieldWidth = (int) canvas.getWidth() - 200;
        playingFieldHeight = (int) canvas.getHeight() - 350;

        positionX = 100;
        positionY = 100;

        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(positionX, positionY, positionX + playingFieldWidth, positionY + playingFieldHeight, paint);
    }

    public void reduceSize() {
        positionX += 10;
        positionY += 10;
        playingFieldWidth -= 20;
        playingFieldHeight -= 20;
    }
}
