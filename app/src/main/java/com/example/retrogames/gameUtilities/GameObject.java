package com.example.retrogames.gameUtilities;

import android.graphics.Canvas;

/**
 * Defines the necessary functionality of each gameObject (Paddle, brick etc..)
 */
public interface GameObject {
    public double getPositionX();
    public double getPositionY();
    public double getHeight();
    public double getLength();
    public double getVelocityX();
    public void draw(Canvas canvas);
}
