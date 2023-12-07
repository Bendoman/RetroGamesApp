package com.example.retrogames.gameUtilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;
import com.example.retrogames.gameUtilities.Joystick;

public class MovablePaddle implements GameObject {

    private final double SPEED_PIXELS_PER_SECOND = 800.0;
    private double MAX_SPEED;

    private double positionX;
    private double positionY;

    private double length;
    private double height;
    private Paint paint;
    private double canvasWidth = 0;

    private double canvasHeight = 0;

    private double velocityX;
    private double velocityY;
    public MovablePaddle(Context context, double positionX, double positionY, double length, double height, double maxUPS) {

        MAX_SPEED = SPEED_PIXELS_PER_SECOND / maxUPS;

        this.positionX = positionX;
        this.positionY = positionY;
        this.length = length;
        this.height = height;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect((float) positionX, (float) positionY, (float) (positionX + length), (float) (positionY + height), paint);
        if(canvasWidth == 0) {
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
        }
    }

    public void update(Joystick joystick) {
        // Times the joystick actuator X value by the maximum speed
        velocityX = joystick.getActuatorX()*MAX_SPEED;

        // So that the paddle stops smoothly at the edges of the screen
        if( (positionX > 0 && positionX < (canvasWidth - length)) || (positionX == 0 && velocityX > 0)
        || (positionX == (canvasWidth - length) && velocityX < 0))
            positionX += velocityX;
        else if( positionX <= 0 )
            positionX = 0;
        else
            positionX = canvasWidth - length;
    }

    public void setPosition(double positionX) {
            this.positionX  = positionX;
    }
    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    public double getHeight() { return height; }
    public double getLength() {return length; }
    public double getVelocityX() { return velocityX; }
}
