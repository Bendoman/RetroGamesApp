package com.example.retrogames.snakeGame;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.GameLoop;

public class Snake {

    private int speed = 5;
    private double positionX;
    private double positionY;
    private double size;

    private Paint paint;

    public Snake(Context context, double positionX, double positionY, double size) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    public void update() {

    }

    public void draw() {

    }
}
