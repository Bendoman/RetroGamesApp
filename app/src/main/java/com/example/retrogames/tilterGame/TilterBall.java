package com.example.retrogames.tilterGame;

import android.content.Context;
import android.hardware.SensorEvent;

import com.example.retrogames.gameUtilities.BouncingBall;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameObject;

import java.util.List;

public class TilterBall extends BouncingBall {

    private float xVelocity;
    private float yVelocity;

    public TilterBall(Context context, GameClass game, double positionX, double positionY, int radius, double maxUPS) {
        super(context, game, positionX, positionY, radius, maxUPS);
    }

    public void update(float xAcceleration, float yAcceleration) {

    }
}