package com.example.retrogames.snakeGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.Joypad;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake {

    private int min;
    private int max;
    private int speed = 5;
    private int positionX;
    private int positionY;
    private GameClass game;
    private int size;

    private Paint paint;
    private int canvasWidth;
    private int canvasHeight;
    private int direction;

    private SnakePlayingField playingField;
    private int playingFieldX;
    private int playingFieldY;
    private int playingFieldWidth;
    private int playingFieldHeight;

    private List<Rect> snakeBody = new ArrayList<Rect>();
    private Rect fruit;
    private boolean validCoordinates;

    public Snake(Context context, Canvas canvas, GameClass game, SnakePlayingField playingField, int size) {
        this.game = game;
        this.size = size;
        this.playingField = playingField;
        this.playingFieldX = playingField.positionX;
        this.playingFieldY = playingField.positionY;
        this.playingFieldWidth = playingField.playingFieldWidth;
        this.playingFieldHeight = playingField.playingFieldHeight;

        int randomXPosition = new Random().nextInt((int) (playingFieldWidth - size) / size);
        int randomYPosition = new Random().nextInt((int) (playingFieldHeight - size) / size);
        positionX = (size * randomXPosition) + 50;
        positionY = (size * randomXPosition) + 50;

        snakeBody.add(new Rect(positionX, positionY - size, positionX + size, positionY));
        addFruit();

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    private void addFruit() {
        while(true) {
            validCoordinates = false;
            int randomXPosition = new Random().nextInt((int) (playingFieldWidth - size) / size);
            int randomYPosition = new Random().nextInt((int) (playingFieldHeight - size) / size);
            int fruitPositionX = (size * randomXPosition) + 50;
            int fruitPositionY = (size * randomYPosition) + 50;

            if (fruitPositionX == positionX && fruitPositionY == positionY)
                validCoordinates = false;

            for (int i = 0; i < snakeBody.size(); i++) {
                validCoordinates = true;
                if (fruitPositionX == snakeBody.get(i).left && fruitPositionY == snakeBody.get(i).top) {
                    validCoordinates = false;
                    break;
                }
            }

            if(validCoordinates) {
                fruit = (new Rect(fruitPositionX, fruitPositionY, fruitPositionX + size, fruitPositionY + size));
                break;
            }
        }
    }

    private void addSegment() {
        Rect tail = snakeBody.get(snakeBody.size() - 1);
        snakeBody.add(new Rect(tail.left, tail.top, tail.right, tail.bottom));
    }

    public void update(Joypad joypad) {
        direction = joypad.getDirection();
        for(int i = snakeBody.size() - 1; i >= 0; i--) {
            if(i == 0)
            {
                snakeBody.get(i).left = positionX;
                snakeBody.get(i).top = positionY;
                snakeBody.get(i).right = positionX + size;
                snakeBody.get(i).bottom = positionY + size;
            }
            else
            {
                snakeBody.get(i).top = snakeBody.get(i - 1).top;
                snakeBody.get(i).bottom = snakeBody.get(i - 1).bottom;

                snakeBody.get(i).left = snakeBody.get(i - 1).left;
                snakeBody.get(i).right = snakeBody.get(i - 1).right;
            }
        }

        switch (direction) {
            case 0:
                positionY -= size;
                break;
            case 1:
                positionY += size;
                break;
            case 2:
                positionX += size;
                break;
            case 3:
                positionX -= size;
                break;
        }

        if(positionX < playingFieldX)
            positionX = playingFieldX + playingFieldWidth - size;
        else if (positionX >= playingFieldX + playingFieldWidth)
            positionX = playingFieldX;

        if(positionY < playingFieldY)
            positionY = playingFieldY + playingFieldHeight - size;
        else if (positionY > playingFieldY + playingFieldHeight)
            positionY = playingFieldY;

        if(positionX == fruit.left && positionY == fruit.top)
        {
            addSegment();
            addFruit();
            game.addScore(1);
        }

        for(int i = 0; i < snakeBody.size(); i++)
        {
            if(positionX == snakeBody.get(i).left && positionY == snakeBody.get(i).top)
                game.gameOver();
        }
    }

    public void draw(Canvas canvas) {
        // Draws each rectangle stored in the snake body arraylist
        for(int i = 0; i < snakeBody.size(); i++)
        {
            Rect segment = snakeBody.get(i);
            if(segment.left >= playingFieldX && segment.left < (playingFieldX + playingFieldWidth) &&
                    segment.top >= playingFieldY && segment.top < (playingFieldY + playingFieldHeight)) {
                paint.setColor(Color.rgb(0, 200, 105));
                canvas.drawRect((float) segment.left, (float) segment.top, (float) segment.right, (float) segment.bottom, paint);
            }
        }

        // Draws the fruit
        paint.setColor(Color.rgb(200, 10, 105));
        canvas.drawRect((float) fruit.left, (float) fruit.top, (float) fruit.right, (float) fruit.bottom, paint);

        // Draw the head last so it is shown overtop of the fruit
        if(positionX >= playingFieldX && positionX < (playingFieldX + playingFieldWidth) &&
                positionY >= playingFieldY && positionY < (playingFieldY + playingFieldHeight)) {
            paint.setColor(Color.rgb(200, 255, 0));
            canvas.drawRect((float) positionX, (float) positionY, (float) (positionX + size), (float) (positionY + size), paint);
        }
    }
}
