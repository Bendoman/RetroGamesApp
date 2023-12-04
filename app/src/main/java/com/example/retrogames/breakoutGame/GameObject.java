package com.example.retrogames.breakoutGame;

import android.graphics.Canvas;

abstract class GameObject {

    private double positionX;
    private double positionY;
    abstract double getPositionX();
    abstract double getPositionY();

    abstract double getHeight();
    abstract double getLength();

    abstract double getVelocityX();

    abstract void draw(Canvas canvas);
}
