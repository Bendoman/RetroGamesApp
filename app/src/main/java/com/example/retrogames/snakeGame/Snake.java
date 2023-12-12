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

/**
 * Implements the game logic for updating the snake, its body
 * and the fruit that must be collected by it.
 */
public class Snake
{
    private final Paint paint;
    private final GameClass game;

    private int positionX;
    private int positionY;
    private final int size;


    private final int playingFieldX;
    private final int playingFieldY;
    private final int playingFieldWidth;
    private final int playingFieldHeight;

    private Rect fruit;
    private final List<Rect> snakeBody = new ArrayList<Rect>();

    public Snake(Context context, GameClass game, SnakePlayingField playingField, int size)
    {
        this.game = game;
        this.size = size;

        // Taking the size parameters from the playingField but not the field itself as the
        // draw method is not called from within the snake class, but the game class
        this.playingFieldX = playingField.positionX;
        this.playingFieldY = playingField.positionY;
        this.playingFieldWidth = playingField.playingFieldWidth;
        this.playingFieldHeight = playingField.playingFieldHeight;

        // Calculates random valid x and y coordinates that conform to an even grid
        // within the playingField as a starting position for the snake
        int randomXPosition = new Random().nextInt((int) (playingFieldWidth - size) / size);
        int randomYPosition = new Random().nextInt((int) (playingFieldHeight - size) / size);
        positionX = (size * randomXPosition) + size;
        positionY = (size * randomYPosition) + size;

        // Populates the snakeBody array with the first segment
        snakeBody.add(new Rect(positionX, positionY - size, positionX + size, positionY));
        // Adds the first fruit to the game
        addFruit();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(context, R.color.player));
    }

    private void addFruit()
    {
        while(true)
        {
            int randomXPosition = new Random().nextInt((int) (playingFieldWidth - size) / size);
            int randomYPosition = new Random().nextInt((int) (playingFieldHeight - size) / size);
            int fruitPositionX = (size * randomXPosition) + size;
            int fruitPositionY = (size * randomYPosition) + size;

            // Generates valid coordinates for the fruit that don't
            // intersect with the snake head or body
            if(fruitPositionX == positionX && fruitPositionY == positionY)
                continue;
            boolean validCoordinates = true;
            for (int i = 0; i < snakeBody.size(); i++) {
                validCoordinates = true;
                if (fruitPositionX == snakeBody.get(i).left && fruitPositionY == snakeBody.get(i).top) {
                    validCoordinates = false;
                    break;
                }
            }

            if(validCoordinates) {
                fruit = (new Rect(fruitPositionX, fruitPositionY,
                        fruitPositionX + size, fruitPositionY + size));
                break;
            }
        }
    }

    // Adds a snake segment to the end of the snake
    private void addSegment() {
        Rect tail = snakeBody.get(snakeBody.size() - 1);
        snakeBody.add(new Rect(tail.left, tail.top, tail.right, tail.bottom));
    }

    // Updates the position of the head and each segment in the tail
    public void update(Joypad joypad)
    {
        int direction = joypad.getDirection();
        // Loops from the end of the list down to the start in order to
        // cascade the positions before the head is updated
        for(int i = snakeBody.size() - 1; i >= 0; i--) {
            if(i == 0) // The head of the snake
            {
                snakeBody.get(i).left = positionX;
                snakeBody.get(i).top = positionY;
                snakeBody.get(i).right = positionX + size;
                snakeBody.get(i).bottom = positionY + size;
            }
            else // The rest of the body
            {
                // Sets each segment to the position of the previous segment in the list
                snakeBody.get(i).top = snakeBody.get(i - 1).top;
                snakeBody.get(i).bottom = snakeBody.get(i - 1).bottom;

                snakeBody.get(i).left = snakeBody.get(i - 1).left;
                snakeBody.get(i).right = snakeBody.get(i - 1).right;
            }
        }

        // Updates the position of the head of the snake based on the direction
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

        // Handles screen wrapping
        if(positionX < playingFieldX)
            positionX = playingFieldX + playingFieldWidth - size;
        else if (positionX >= playingFieldX + playingFieldWidth)
            positionX = playingFieldX;

        if(positionY < playingFieldY)
            positionY = playingFieldY + playingFieldHeight - size;
        else if (positionY >= playingFieldY + playingFieldHeight)
            positionY = playingFieldY;

        // Re-creates the fruit, adds a segment and updates
        // the score when the head intersects a fruit
        if(positionX == fruit.left && positionY == fruit.top)
        {
            addSegment();
            addFruit();
            game.addScore(1);
        }

        // If the head of the snake collides with any of the elements in the body the game is over
        for(int i = 0; i < snakeBody.size(); i++)
        {
            if(positionX == snakeBody.get(i).left && positionY == snakeBody.get(i).top)
                game.gameOver();
        }
    }

    public void draw(Canvas canvas)
    {
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
