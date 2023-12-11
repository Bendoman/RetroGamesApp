package com.example.retrogames.snakeGame;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;
import com.example.retrogames.gameUtilities.GameOver;
import com.example.retrogames.gameUtilities.Joypad;

@SuppressLint("ViewConstructor")
public class SnakeGame extends SurfaceView implements SurfaceHolder.Callback, GameClass
{
    // The minimum distance for a swipe gesture to be registered
    private static final float MIN_DISTANCE = 15;
    // Used for detecting the length of a swipe gesture
    private float x1;
    private float y1;

    private Snake snake;
    private Joypad joypad;
    private GameOver gameOverText;
    private final GameLoop gameLoop;
    private final SnakeMainActivity main;
    private SnakePlayingField playingField;

    private int score = 0;
    private int level = 1;
    public boolean isRunning = true;

    public SnakeGame(Context context, SnakeMainActivity main)
    {
        super(context);
        this.main = main;

        // Get surface holder and callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Instantiates the game loop
        gameLoop = new GameLoop(this, surfaceHolder, 4);
        setFocusable(true);
    }

    // Initialize game objects
    @Override
    public void initObjects(Canvas canvas)
    {
        int snakeSize = 50;
        playingField = new SnakePlayingField(canvas, snakeSize);
        snake = new Snake(getContext(),this, playingField, snakeSize);

        gameOverText = new GameOver(canvas, getContext());
        joypad = new Joypad(canvas.getWidth()/2f - 50, canvas.getHeight() - 250);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // Handles touch and gesture actions
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // Passes the current x and y coordinates of the mouse, the JoyPad will calculate if
                // they intersect any of the control surfaces and Update the direction accordingly
                joypad.calculateDirection((double) event.getX(), (double) event.getY());

                // For detecting swipe gesture
                x1 = event.getX();
                y1 = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                // Dealing with swipe gesture
                float x2 = event.getX();
                float y2 = event.getY();

                // If the game is over this code will allow the
                // user to press the menu buttons that appear
                if (!isRunning)
                {
                    GameOver g = gameOverText;
                    if(x2 > g.retryLeft && x2 < g.retryRight && y2 > g.retryTop && y2 < g.retryBottom)
                        main.restart();
                    else if(x2 > g.backLeft && x2 < g.backRight && y2 > g.backTop && y2 < g.backBottom)
                        main.finishActivity();
                }

                // Change in position
                float deltaX = x1 - x2;
                float deltaY = y1 - y2;

                // Only checking swipe gestures if they start within the playing field.
                // Interfacing with the JoyPad buttons so that there the same safeguard logic for
                // direction is still in play, and so that there is visual feedback on the JoyPad
                // of the direction the snake is moving.
                if(x1 > playingField.positionX && x1 < playingField.positionX + playingField.playingFieldWidth &&
                    y1 > playingField.positionY && y1 < playingField.positionY + playingField.playingFieldHeight)
                {
                    if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (x2 < x1)
                            joypad.calculateDirection(joypad.leftArrow.left + 1, joypad.leftArrow.top + 1);
                        else
                            joypad.calculateDirection(joypad.rightArrow.left + 1, joypad.rightArrow.top + 1);
                    } else if (Math.abs(deltaY) > MIN_DISTANCE && Math.abs(deltaY) > Math.abs(deltaX)) {
                        if (y2 < y1)
                            joypad.calculateDirection(joypad.upArrow.left + 1, joypad.upArrow.top + 1);
                        else
                            joypad.calculateDirection(joypad.downArrow.left + 1, joypad.downArrow.top + 1);
                    }
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    // Starts the GameLoop when the surface is created
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { Log.d(".java", "loop started()"); gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    // Ends the game if the surface is destroyed to avoid crashes
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) { endGame(); }

    @Override
    public void update(Canvas canvas) { snake.update(joypad); }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        drawScore(canvas);
        snake.draw(canvas);
        joypad.draw(canvas);
        playingField.draw(canvas);

        if(!isRunning) {
            // Draws the game over menu if the game has stopped
            gameOverText.draw(canvas);
            endGame(); // Ends the game loop
        }
    }

    // Draws the game score and level values
    public void drawScore(Canvas canvas)
    {
        Paint paint = new Paint();
        String score = Integer.toString(this.score);
        String level = Integer.toString(this.level);

        paint.setColor(ContextCompat.getColor(getContext(), R.color.deep_magenta));
        paint.setTextSize(40);
        canvas.drawText("SCORE: " + score, (canvas.getWidth()/2f + 200), canvas.getHeight() - 115, paint);
        canvas.drawText("LEVEL: " + level, (canvas.getWidth()/2f + 200), canvas.getHeight() - 45, paint);
    }

    // Stop updating the game
    @Override
    public void endGame() { gameLoop.endLoop(); }

    @Override
    public void gameOver() {
        isRunning = false;
    }

    // Adds the passed score and increases the speed of the game every five levels
    @Override
    public void addScore(int i) {
        score += i;
        if(gameLoop.maxUPS < 10 && score % 5 == 0) {
            gameLoop.setUPS(gameLoop.maxUPS + 0.5);
            level++;
        }
    }

    @Override
    public double getScore() { return score; }

    @Override
    public void removeObject(GameObject rect) {}
}
