package com.example.retrogames.gameUtilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

public class MovablePaddle implements GameObject
{
    private final double MAX_SPEED;

    private double positionX;
    private double velocityX;
    // For the purposes of these games the Y position is locked
    private final double positionY;

    // Length changes based on level in Pong, height is always the same
    private double length;
    private final double height;

    private final Paint paint;
    private double canvasWidth = 0;

    public MovablePaddle(Context context, double positionX, double positionY, double length, double height, double maxUPS)
    {
        // Ties the max speed of the paddle to the UPS so that we can
        // determine precisely the value of pixels it will move per second
        double SPEED_PIXELS_PER_SECOND = 800.0;
        MAX_SPEED = SPEED_PIXELS_PER_SECOND / maxUPS;

        this.length = length;
        this.height = height;
        this.positionX = positionX;
        this.positionY = positionY;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect((float) positionX, (float) positionY, (float) (positionX + length), (float) (positionY + height), paint);

        // On the first draw call of the loop, canvasWidth will not be set
        // this sets it so it can be used in the update method
        if(canvasWidth == 0)
            canvasWidth = canvas.getWidth();
    }

    public void update(Joystick joystick)
    {
        // Times the joystick actuator X value by the maximum speed
        velocityX = joystick.getActuatorX()*MAX_SPEED;

        // So that the paddle stops smoothly at the edges of the screen
        if( (positionX > 0 && positionX < (canvasWidth - length)) || (positionX == 0 && velocityX > 0)
        || (positionX == (canvasWidth - length) && velocityX < 0)) {
            positionX += velocityX;
        }
        else if( positionX <= 0 ) {
            positionX = 0;
        }
        else {
            positionX = canvasWidth - length;
        }
    }

    // Utility methods
    public void reduceSize() { this.length -= 25; }

    public double getHeight() { return height; }
    public double getLength() {return length; }
    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    public double getVelocityX() { return velocityX; }
}
