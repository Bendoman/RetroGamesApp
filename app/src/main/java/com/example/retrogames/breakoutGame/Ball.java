package com.example.retrogames.breakoutGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

import java.util.List;

public class Ball {

    // Tying the maximum speed to number of pixel per second by relating it to the UPS
    private double pixelsPerSecond = 400.0;
    private double maxSpeed = pixelsPerSecond / GameLoop.MAX_UPS;

    // For updating the maximum speed as the level increases
    public void increaseSpeed() {
        if(pixelsPerSecond > 1000)
            return;

        pixelsPerSecond += 250;
        maxSpeed = pixelsPerSecond / GameLoop.MAX_UPS;
    }

    private BreakoutGame game;
    private List<GameObject> gameObjects;
    private double canvasWidth = 0;
    private double canvasHeight = 0;
    private double positionX;
    private double positionY;
    private double velocityX = maxSpeed;
    private double velocityY = maxSpeed;
    private int radius;

    private Paint paint;


    public Ball(Context context, BreakoutGame game,  List<GameObject> gameObjects, double positionX, double positionY, int radius) {
        this.game = game;
        this.gameObjects = gameObjects;
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, radius, paint);
        if(canvasWidth == 0) {
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
        }
    }

    public void update() {
        // Checks if the ball is colliding with the edges of the screen and reverses its velocity if so
        if(positionX - radius <= 0 || positionX + radius >= canvasWidth)
            velocityX = -velocityX;
        if(positionY - radius <= 0 || positionY + radius >= canvasHeight)
            velocityY = -velocityY;

        // This for loop is for detecting collisions with the game objects ( Bricks and paddle )
        for(int i = 0; i < gameObjects.size(); i++)
        {
            GameObject rect = gameObjects.get(i);
            if(i == 0 && (positionY + radius) > (rect.getPositionY() + rect.getHeight() + 100))
                game.endGame();

            if(intersects(rect))
            {
                if (positionX < rect.getPositionX() || positionX > rect.getPositionX()+rect.getLength())
                    velocityX = -velocityX;
                velocityY = -velocityY;

                if(i > 0) {
                    game.removeObject(rect);
                    game.addScore(1);
                }
            }
        }

        positionX += velocityX;
        positionY += velocityY;
    }

    /*
        Standard circle - rectangle collision detection.
        Returns true if this object is in collision with the passed rectangle GameObject
     */
    boolean intersects(GameObject rect)
    {
        double testX = positionX;
        double testY = positionY;

        if (positionX < rect.getPositionX())
            testX = rect.getPositionX();
        else if (positionX > rect.getPositionX()+rect.getLength())
            testX = rect.getPositionX()+rect.getLength();

        if (positionY < rect.getPositionY())
            testY = rect.getPositionY();
        else if (positionY > rect.getPositionY()+rect.getHeight())
            testY = rect.getPositionY()+rect.getHeight();

        // get distance from closest edges
        double distX = positionX-testX;
        double distY = positionY-testY;
        double distance = Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the radius, collision!
        return distance <= radius;
    }
}
