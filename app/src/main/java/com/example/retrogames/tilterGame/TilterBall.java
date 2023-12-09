package com.example.retrogames.tilterGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.SensorEvent;
import android.util.Log;

import com.example.retrogames.gameUtilities.BouncingBall;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameObject;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TilterBall extends BouncingBall {

    private final int playingFieldWidth;
    private final int playingFieldHeight;
    private TilterPlayingField playingField;
    private Rect fruit;
    private int fruitRadius = 20;
    private int fruitPositionX;
    private int fruitPositionY;

    public TilterBall(Context context, GameClass game, double positionX, double positionY,
                      int radius, double maxUPS, TilterPlayingField playingField) {
        super(context, game, positionX, positionY, radius, maxUPS);
        this.playingField = playingField;
        playingFieldWidth = playingField.playingFieldWidth;
        playingFieldHeight = playingField.playingFieldHeight;
        addFruit();
    }

    public void update(float xAcceleration, float yAcceleration) {
        // Waits to update until the first time the ball is drawn so that it has correct canvas dimensions
        if(canvasWidth == 0)
            return;

        positionX += (xAcceleration*1.5);
        positionY += (yAcceleration*1.5);

        if(positionX < playingField.positionX - radius)
            game.gameOver();
        else if(positionX > playingField.positionX + playingFieldWidth + radius)
            game.gameOver();

        if(positionY < playingField.positionY - radius)
            game.gameOver();
        else if(positionY > playingField.positionY + playingFieldHeight + radius)
            game.gameOver();

        if(circleCollision((float) positionX, (float) positionY, radius, fruitPositionX, fruitPositionY, fruitRadius)) {
            addFruit();
            game.addScore(1);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(fruitPositionX != 0 && fruitPositionY != 0) {
            paint.setColor(Color.rgb(200, 10, 105));
            canvas.drawCircle(fruitPositionX, fruitPositionY, 20, paint);
        }
    }

    private void addFruit() {
        int min, max;
        min = playingField.positionX + radius;
        max = playingFieldWidth - radius;
        fruitPositionX = ThreadLocalRandom.current().nextInt(min, max + 1);

        min = playingField.positionY + radius;
        max = playingFieldWidth - radius;
        fruitPositionY = ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private boolean circleCollision(float x1, float y1, float r1, float x2, float y2, float r2)
    {
        // get distance between the circle's centers
        // use the Pythagorean Theorem to compute the distance
        float distX = x1 - x2;
        float distY = y1 - y2;
        float distance = (float) Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the sum of the circle's
        // radii, the circles are touching!
        return distance <= r1 + r2;
    }

    public void reduceFieldSize() {
        playingField.reduceSize();
    }
}
