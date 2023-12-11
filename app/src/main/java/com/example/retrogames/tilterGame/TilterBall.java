package com.example.retrogames.tilterGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.example.retrogames.gameUtilities.BouncingBall;
import com.example.retrogames.gameUtilities.GameClass;

import java.util.concurrent.ThreadLocalRandom;

public class TilterBall extends BouncingBall
{
    private int fruitPositionX;
    private int fruitPositionY;
    private final int fruitRadius = 20;
    private final int playingFieldWidth;
    private final int playingFieldHeight;
    private final TilterPlayingField playingField;

    // Using the second BouncingBall constructor that doesn't require a gameObjects list
    public TilterBall(Context context, GameClass game, double positionX, double positionY,
                      int radius, double maxUPS, TilterPlayingField playingField) {
        super(context, game, positionX, positionY, radius, maxUPS);

        // Appending to the default bouncing ball constructor as the tilter game has a PlayingField
        // that determines the update logic of the ball
        this.playingField = playingField;
        playingFieldWidth = playingField.playingFieldWidth;
        playingFieldHeight = playingField.playingFieldHeight;

        // Adds the first fruit to the game
        addFruit();
    }

    public void update(float xAcceleration, float yAcceleration) {
        // Waits to update until the first time the ball is drawn so that it has correct canvas dimensions
        if(canvasWidth == 0)
            return;

        // Updates the balls position based on the acceleration of the devices gyroscopes
        positionX += (xAcceleration*1.15);
        positionY += (yAcceleration*1.15);

        // Ends the game if the ball leaves the bounds of the playing field
        if(positionX < playingField.positionX - radius)
            game.gameOver();
        else if(positionX > playingField.positionX + playingFieldWidth + radius)
            game.gameOver();

        if(positionY < playingField.positionY - radius)
            game.gameOver();
        else if(positionY > playingField.positionY + playingFieldHeight + radius)
            game.gameOver();

        // If the ball collides with the fruit objective then re-create the fruit and add score
        if(circleCollision((float) positionX, (float) positionY, radius, fruitPositionX, fruitPositionY, fruitRadius)) {
            addFruit();
            game.addScore(1);
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        // The add fruit method uses the canvasWidth and height
        // properties which are null when the object is first created
        if(fruitPositionX != 0 && fruitPositionY != 0)
        {
            paint.setColor(Color.rgb(200, 10, 105));
            canvas.drawCircle(fruitPositionX, fruitPositionY, fruitRadius, paint);
        }
    }

    // Determines a valid position to generate a fruit within the playing field boundaries
    private void addFruit()
    {
        int min, max;
        min = playingField.positionX + radius;
        max = playingFieldWidth - radius;
        fruitPositionX = ThreadLocalRandom.current().nextInt(min, max + 1);

        min = playingField.positionY + radius;
        max = playingFieldWidth - radius;
        fruitPositionY = ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // Standard circle collision math
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
