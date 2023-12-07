package com.example.retrogames.snakeGame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

public class PlayingField {
    public Paint paint;
    public int positionX;
    public int positionY;
    public int playingFieldWidth;
    public int playingFieldHeight;

    public PlayingField(Canvas canvas) {
        playingFieldWidth = (int) canvas.getWidth() - 50;
        playingFieldHeight = (int) canvas.getHeight() - 350;

        while(playingFieldWidth % 50 != 0)
            playingFieldWidth--;
        while(playingFieldHeight % 50 != 0)
            playingFieldHeight--;

        positionX = 50;
        positionY = 50;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(positionX, positionY, positionX + playingFieldWidth, positionY + playingFieldHeight, paint);
    }
}
