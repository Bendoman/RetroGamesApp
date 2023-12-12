package com.example.retrogames.snakeGame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Playing field object that defines the grid in which the snake can be drawn
 */
public class SnakePlayingField
{
    public Paint paint;
    public int positionX;
    public int positionY;
    public int playingFieldWidth;
    public int playingFieldHeight;

    public SnakePlayingField(Canvas canvas, int snakeSize)
    {
        playingFieldWidth = (int) canvas.getWidth() - 100;
        playingFieldHeight = (int) canvas.getHeight() - 350;

        // For the snake game logic to be valid the playing field's size
        // must be evenly divisible by the size of each segment of the snake and the fruit
        while(playingFieldWidth % snakeSize != 0)
            playingFieldWidth--;
        while(playingFieldHeight % snakeSize != 0)
            playingFieldHeight--;

        positionX = 50;
        positionY = 50;

        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(positionX, positionY, positionX + playingFieldWidth,
                positionY + playingFieldHeight, paint);
    }
}
