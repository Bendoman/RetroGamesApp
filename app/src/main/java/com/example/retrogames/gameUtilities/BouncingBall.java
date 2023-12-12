package com.example.retrogames.gameUtilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

import java.util.List;

/**
 * Bouncing ball object that is extended by the ball objects for each game.
 * Implements the base functionality relevant to each.
 */
public class BouncingBall
{
    // Tying the maximum speed to number of pixel per second by relating it to the UPS
    private double pixelsPerSecond = 400.0;
    private double maxSpeed;
    protected double maxUPS;

    private final int color1;
    private final int color2;
    private final int color3;

    protected Paint paint;
    protected GameClass game;
    protected List<GameObject> gameObjects;

    protected Context context;

    protected int radius;
    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;
    protected double canvasWidth = 0;
    protected double canvasHeight = 0;

    // Implementing two constructors as not all games include a list of objects to pass
    public BouncingBall(Context context, GameClass game, List<GameObject> gameObjects, double positionX, double positionY, int radius, double maxUPS)
    {
        this.maxUPS = maxUPS;
        maxSpeed = pixelsPerSecond / maxUPS;
        velocityX = maxSpeed;
        velocityY = maxSpeed;

        this.game = game;
        this.radius = radius;
        this.context = context;
        this.positionX = positionX;
        this.positionY = positionY;
        this.gameObjects = gameObjects;

        paint = new Paint();
        color1 = ContextCompat.getColor(context, R.color.ball_outer);
        color2 = ContextCompat.getColor(context, R.color.ball_second_ring);
        color3 = ContextCompat.getColor(context, R.color.ball_inner);
        paint.setColor(color1);
    }

    public BouncingBall(Context context, GameClass game, double positionX, double positionY, int radius, double maxUPS)
    {
        this.maxUPS = maxUPS;
        maxSpeed = pixelsPerSecond / maxUPS;
        velocityX = maxSpeed;
        velocityY = maxSpeed;

        this.game = game;
        this.radius = radius;
        this.positionX = positionX;
        this.positionY = positionY;

        paint = new Paint();
        color1 = ContextCompat.getColor(context, R.color.ball_outer);
        color2 = ContextCompat.getColor(context, R.color.ball_second_ring);
        color3 = ContextCompat.getColor(context, R.color.ball_inner);
        paint.setColor(color1);
    }

    public void draw(Canvas canvas)
    {
        // Assigns local canvas width and height variables as this information is not available
        // to the game class at the time of instantiating the ball object
        if(canvasWidth == 0) {
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
        }

        // Prints three concentric circles for shading purposes
        paint.setColor(color1);
        canvas.drawCircle((float) positionX, (float) positionY, radius, paint);
        paint.setColor(color2);
        canvas.drawCircle((float) positionX, (float) positionY, (float) (radius/1.4), paint);
        paint.setColor(color3);
        canvas.drawCircle((float) positionX, (float) positionY, (float) (radius/2), paint);
    }

    // Only includes wall collision logic as its the same for all games
    public void update() {
        // Checks if the ball is colliding with the edges of the screen and reverses its velocity if so
        if(positionX - radius <= 0 || positionX + radius >= canvasWidth && canvasWidth != 0)
        {
            // Stops the ball from getting stuck in the wall
            if(positionX - radius < 0)
                positionX = radius + 1;
            else if(positionX + radius > canvasWidth)
                positionX = canvasWidth - radius - 1;

            velocityX = -velocityX;
        }

        if(positionY - radius <= 0 || positionY + radius >= canvasHeight && canvasWidth != 0)
        {
            // Stops the ball from getting stuck in the wall
            if(positionY - radius < 0)
                positionY = radius + 1;
            else if(positionY + radius > canvasHeight)
                positionY = canvasWidth - radius - 1;

            velocityY = -velocityY;
        }

        // Does not update the velocity as each sub class overloads the update function
        // and includes that section themselves.
    }


    // Standard circle - rectangle collision detection.
    // Returns true if this object is in collision with the passed rectangle GameObject
    protected boolean intersects(GameObject rect)
    {
        double tempX = positionX;
        double tempY = positionY;

        // Calculates which edge is closest
        if (positionX < rect.getPositionX())
            tempX = rect.getPositionX();
        else if (positionX > rect.getPositionX()+rect.getLength())
            tempX = rect.getPositionX()+rect.getLength();

        if (positionY < rect.getPositionY())
            tempY = rect.getPositionY();
        else if (positionY > rect.getPositionY()+rect.getHeight())
            tempY = rect.getPositionY()+rect.getHeight();

        // get distance from closest edges
        double distX = positionX-tempX;
        double distY = positionY-tempY;
        double distance = Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the radius, collision!
        return distance <= radius;
    }

    // Increases the ball speed
    public void increaseSpeed()
    {
        if(pixelsPerSecond > 1000)
            return;
        pixelsPerSecond += 250;
        maxSpeed = pixelsPerSecond / maxUPS;
    }
}
