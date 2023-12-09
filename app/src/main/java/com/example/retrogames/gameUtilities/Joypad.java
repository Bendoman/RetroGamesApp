package com.example.retrogames.gameUtilities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Joypad {
    private double positionX;
    private double positionY;

    private double width = 100;
    private double height = 100;
    private boolean isPressed;

    private Paint upPaint;
    private Paint downPaint;
    private Paint leftPaint;
    private Paint rightPaint;

    public Rect upArrow;
    public Rect downArrow;
    public Rect leftArrow;
    public Rect rightArrow;
    private int direction;
    private boolean calculated;


    public Joypad(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;

        Rect rect = new Rect();

        upArrow = new Rect((int) positionX, (int) positionY, (int) (positionX + width), (int) (positionY + height));
        downArrow = new Rect((int) positionX, (int) positionY + 100, (int) (positionX + width), (int) (positionY + 100 + height));
        leftArrow = new Rect((int) positionX - 100, (int) positionY + 100, (int) (positionX - 100 + width), (int) (positionY + 100 + height));
        rightArrow = new Rect((int) positionX + 100, (int) positionY + 100, (int) (positionX + 100 + width), (int) (positionY + 100 + height));

        // Paint for JoyPad segments
        upPaint = new Paint();
        upPaint.setColor(Color.WHITE);
        upPaint.setStyle(Paint.Style.STROKE);
        upPaint.setStrokeWidth(3);

        downPaint = new Paint();
        downPaint.setColor(Color.WHITE);
        downPaint.setStyle(Paint.Style.STROKE);
        downPaint.setStrokeWidth(3);

        leftPaint = new Paint();
        leftPaint.setColor(Color.WHITE);
        leftPaint.setStyle(Paint.Style.STROKE);
        leftPaint.setStrokeWidth(3);

        rightPaint = new Paint();
        rightPaint.setColor(Color.WHITE);
        rightPaint.setStyle(Paint.Style.STROKE);
        rightPaint.setStrokeWidth(3);
    }

    public void update() {
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(upArrow.left, upArrow.top, upArrow.right, upArrow.bottom, upPaint);
        canvas.drawRect(downArrow.left, downArrow.top, downArrow.right, downArrow.bottom, downPaint);
        canvas.drawRect(leftArrow.left, leftArrow.top, leftArrow.right, leftArrow.bottom, leftPaint);
        canvas.drawRect(rightArrow.left, rightArrow.top, rightArrow.right, rightArrow.bottom, rightPaint);
    }

    public void calculateDirection(double x, double y) {
        // Doing this check so that the direction is only calculated after the game loop actually uses the value
        // tying it to UPS and stopping the player from being able to press multiple times quickly and reverse the velocity
        if(!calculated)
            return;

        if(x > upArrow.left && x < upArrow.right && y > upArrow.top && y < upArrow.bottom && direction != 1) {
            resetColours();
            upPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            direction = 0;
        }
        if(x > downArrow.left && x < downArrow.right && y > downArrow.top && y < downArrow.bottom && direction != 0) {
            resetColours();
            downPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            direction = 1;
        }
        if(x > rightArrow.left && x < rightArrow.right && y > rightArrow.top && y < rightArrow.bottom && direction != 3) {
            resetColours();
            rightPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            direction = 2;
        }
        if(x > leftArrow.left && x < leftArrow.right && y > leftArrow.top && y < leftArrow.bottom && direction != 2) {
            resetColours();
            leftPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            direction = 3;
        }

        calculated = false;
    }

    public int getDirection() {
        calculated = true;
        return direction;
    }

    public void resetColours() {
        upPaint.setStyle(Paint.Style.STROKE);
        downPaint.setStyle(Paint.Style.STROKE);
        leftPaint.setStyle(Paint.Style.STROKE);
        rightPaint.setStyle(Paint.Style.STROKE);
    }
}
