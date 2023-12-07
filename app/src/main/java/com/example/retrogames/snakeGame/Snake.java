package com.example.retrogames.snakeGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.Joypad;
import com.example.retrogames.gameUtilities.Joystick;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Snake {

    private int min;
    private int max;
    private int speed = 5;
    private double positionX;
    private double positionY;
    private int size;

    private Paint paint;
    private int canvasWidth;
    private int canvasHeight;
    private int direction;

    private PlayingField playingField;
    private double playingFieldX;
    private double playingFieldY;
    private double playingFieldWidth;
    private double playingFieldHeight;

    public Snake(Context context, Canvas canvas, PlayingField playingField, int size) {
        this.size = size;
        this.playingField = playingField;
        this.playingFieldX = playingField.positionX;
        this.playingFieldY = playingField.positionY;
        this.playingFieldWidth = playingField.playingFieldWidth;
        this.playingFieldHeight = playingField.playingFieldHeight;

        int randomXPosition = new Random().nextInt((int) (playingFieldWidth - size) / size);
        int randomYPosition = new Random().nextInt((int) (playingFieldHeight - size) / size);
        positionX = (size * randomXPosition) + 50;
        positionY = (size * randomXPosition) + 50;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public void update(Joypad joypad) {
        direction = joypad.getDirection();

        switch (direction) {
            case 0:
                positionY -= size;
                break;
            case 1:
                positionY += size;
                break;
            case 2:
                positionX += size;
                break;
            case 3:
                positionX -= size;
                break;
        }

        if(positionX < playingFieldX)
            positionX = playingFieldX + playingFieldWidth - size;
        else if (positionX > playingFieldX + playingFieldWidth)
            positionX = playingFieldX;

        if(positionY < playingFieldY)
            positionY = playingFieldY + playingFieldHeight - size;
        else if (positionY > playingFieldY + playingFieldHeight)
            positionY = playingFieldY;
    }

    public void draw(Canvas canvas) {
        if(positionX >= playingFieldX && positionX < (playingFieldX + playingFieldWidth) &&
           positionY >= playingFieldY && positionY < (playingFieldY + playingFieldHeight)) {
            canvas.drawRect((float) positionX, (float) positionY, (float) (positionX + size), (float) (positionY + size), paint);
        }
    }
}
