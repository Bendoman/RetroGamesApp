package com.example.retrogames.gameUtilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

public class Joystick {
    private final Paint innerCirclePaint;
    private final Paint outerCirclePaint;

    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;

    private final int outerCircleRadius;
    private final int outerCircleCenterPositionX;
    private final int outerCircleCenterPositionY;

    private final int innerCircleRadius;
    private int innerCircleCenterPositionX;
    private int innerCircleCenterPositionY;

    public Joystick(Context context, int centerPositionX, int centerPositionY, int outerCircleRadius, int innerCircleRadius)
    {
        // Setting the x and y for the outer and inner circles
        outerCircleCenterPositionX = centerPositionX;
        outerCircleCenterPositionY = centerPositionY;
        innerCircleCenterPositionX = centerPositionX;
        innerCircleCenterPositionY = centerPositionY;

        // Circle Radii
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        // Paint for circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(ContextCompat.getColor(context, R.color.joystick_outer));
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(ContextCompat.getColor(context, R.color.joystick_inner));
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas)
    {
        // Outer circle
        canvas.drawCircle(
                outerCircleCenterPositionX,
                outerCircleCenterPositionY,
                outerCircleRadius,
                outerCirclePaint
        );
        // Inner circle
        canvas.drawCircle(
                innerCircleCenterPositionX,
                innerCircleCenterPositionY,
                innerCircleRadius,
                innerCirclePaint
        );
    }

    public void update() {
        updateInnerCirclePosition();
    }

    // Updates the inner circle position based on the actuator values
    private void updateInnerCirclePosition()
    {
        innerCircleCenterPositionX = (int) (outerCircleCenterPositionX + actuatorX*outerCircleRadius);
        innerCircleCenterPositionY = (int) (outerCircleCenterPositionY + actuatorY*outerCircleRadius);
    }

    // Determines if the given X and Y coordinates intersect the outer circle
    public boolean isPressed(double touchPositionX, double touchPositionY)
    {
        double joystickCenterToTouchDistance = Math.sqrt(
                Math.pow(outerCircleCenterPositionX - touchPositionX, 2) +
                        Math.pow(outerCircleCenterPositionY - touchPositionY, 2)
        );
        return joystickCenterToTouchDistance < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() { return isPressed; }

    // Sets the actuator X and Y based on the touch X and Y
    public void setActuator(double touchPositionX, double touchPositionY)
    {
        double deltaX = touchPositionX - outerCircleCenterPositionX;
        double deltaY = touchPositionY - outerCircleCenterPositionY;
        double deltaDistance = Math.sqrt(
                Math.pow(deltaX, 2) +
                Math.pow(deltaY, 2)
        );

        if(deltaDistance < outerCircleRadius) {
            actuatorX = deltaX/outerCircleRadius;
            actuatorY = deltaY/outerCircleRadius;
        } else {
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }
    }

    // Resets the actuator values, returning the inner circle to the center
    public void resetActuator() {
        actuatorX = 0.0;
        actuatorY = 0.0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
