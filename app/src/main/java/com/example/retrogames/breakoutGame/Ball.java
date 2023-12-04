package com.example.retrogames.breakoutGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

import java.util.List;

public class Ball {

    private static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    private BreakoutGame game;
    private List<GameObject> gameObjects;
    private double canvasWidth = 0;
    private double canvasHeight = 0;
    private double positionX;
    private double positionY;
    private double velocityX = MAX_SPEED;
    private double velocityY = MAX_SPEED;
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
        if(positionX - radius <= 0 || positionX + radius >= canvasWidth)
            velocityX = -velocityX;
        if(positionY - radius <= 0 || positionY + radius >= canvasHeight)
            velocityY = -velocityY;

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

    boolean intersects(GameObject rect)
    {
        // temporarect.getPositionY() variables to set edges for testing
        double testX = positionX;
        double testY = positionY;

        // which edge is closest?
        if (positionX < rect.getPositionX())
            testX = rect.getPositionX();      // test left edge
        else if (positionX > rect.getPositionX()+rect.getLength())
            testX = rect.getPositionX()+rect.getLength();   // right edge

        if (positionY < rect.getPositionY())
            testY = rect.getPositionY();      // top edge
        else if (positionY > rect.getPositionY()+rect.getHeight())
            testY = rect.getPositionY()+rect.getHeight();   // bottom edge

        // get distance from closest edges
        double distX = positionX-testX;
        double distY = positionY-testY;
        double distance = Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the radius, collision!
        return distance <= radius;
    }
}
