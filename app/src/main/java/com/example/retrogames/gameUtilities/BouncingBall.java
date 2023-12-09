package com.example.retrogames.gameUtilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

import java.util.List;

public class BouncingBall {

    protected final double maxUPS;
    private final int color2;
    private int color1;
    // Tying the maximum speed to number of pixel per second by relating it to the UPS
    private double pixelsPerSecond = 400.0;
    private double maxSpeed;

    // For updating the maximum speed as the level increases
    public void increaseSpeed() {
        if(pixelsPerSecond > 1000)
            return;

        pixelsPerSecond += 250;
        maxSpeed = pixelsPerSecond / maxUPS;
    }

    protected GameClass game;
    protected List<GameObject> gameObjects;
    protected double canvasWidth = 0;
    protected double canvasHeight = 0;
    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;
    protected int radius;
    protected Context context;
    protected Paint paint;


    public BouncingBall(Context context, GameClass game, List<GameObject> gameObjects, double positionX, double positionY, int radius, double maxUPS) {
        this.maxUPS = maxUPS;
        maxSpeed = pixelsPerSecond / maxUPS;
        velocityX = maxSpeed;
        velocityY = maxSpeed;

        this.game = game;
        this.gameObjects = gameObjects;
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.context = context;

        paint = new Paint();
        color1 = ContextCompat.getColor(context, R.color.ball_outer);
        color2 = ContextCompat.getColor(context, R.color.ball_inner);
        paint.setColor(color1);
    }

    public BouncingBall(Context context, GameClass game, double positionX, double positionY, int radius, double maxUPS) {
        this.maxUPS = maxUPS;
        maxSpeed = pixelsPerSecond / maxUPS;
        velocityX = maxSpeed;
        velocityY = maxSpeed;

        this.game = game;
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;

        paint = new Paint();
        color1 = ContextCompat.getColor(context, R.color.ball_outer);
        color2 = ContextCompat.getColor(context, R.color.ball_inner);
        paint.setColor(color1);
    }

    public void draw(Canvas canvas) {
        paint.setColor(color1);
        canvas.drawCircle((float) positionX, (float) positionY, radius, paint);
        paint.setColor(color2);
        canvas.drawCircle((float) positionX, (float) positionY, (float) (radius/1.35), paint);

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

            if(intersects(rect))
            {
                if (rect.getVelocityX() > 0) // object came from the left
                    positionX = rect.getPositionX() - radius;
                else if (rect.getVelocityX() < 0) // object came from the right
                    positionX = rect.getPositionX() + rect.getLength() + radius;

                if (positionX < rect.getPositionX() || positionX > rect.getPositionX()+rect.getLength())
                    velocityX = -velocityX;
                velocityY = -velocityY;
            }
        }

        positionX += velocityX;
        positionY += velocityY;
    }

    /*
        Standard circle - rectangle collision detection.
        Returns true if this object is in collision with the passed rectangle GameObject
     */
    protected boolean intersects(GameObject rect)
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
