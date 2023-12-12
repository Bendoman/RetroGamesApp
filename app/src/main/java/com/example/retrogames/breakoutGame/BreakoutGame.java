package com.example.retrogames.breakoutGame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import com.example.retrogames.gameUtilities.Joystick;
import com.example.retrogames.gameUtilities.MovablePaddle;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles initializing, updating and drawing all objects related to the breakout game
 */
@SuppressLint("ViewConstructor")
public class BreakoutGame extends SurfaceView implements SurfaceHolder.Callback, GameClass
{
    private static final int MAX_BRICK_LAYERS = 10;

    private BreakoutBall ball;
    private Joystick joystick;
    private MovablePaddle player;
    private GameOver gameOverText;
    private List<GameObject> gameObjects;

    private final GameLoop gameLoop;
    private final BreakoutMainActivity main;

    private int score = 0;
    private int level = 1;
    private int brickLayers = 1;
    private boolean isRunning = true;

    public BreakoutGame(Context context, BreakoutMainActivity main)
    {
        super(context);
        this.main = main;

        // Get surface holder and callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Instantiates the game loop
        gameLoop = new GameLoop(this, surfaceHolder, 60);
        setFocusable(true);
    }

    public void initObjects(Canvas canvas)
    {
        // Initialize game objects
        gameObjects = new ArrayList<GameObject>();
        gameOverText = new GameOver(canvas, getContext());
        joystick = new Joystick(getContext(), canvas.getWidth()/2, canvas.getHeight()-140, 70, 40);
        player = new MovablePaddle(getContext(), 500, 1500, 250, 50, gameLoop.maxUPS);
        gameObjects.add(player);

        // Add the first layer of bricks
        initBricks(canvas);
        ball = new BreakoutBall(getContext(), this, gameObjects, 600,  600, 25, 60);
    }

    public void initBricks(Canvas canvas)
    {
        // Creates a row of brick objects for every brick layer and adds them to gameObjects
        for (int x = 0; x < brickLayers; x ++)
        {
            for(int i = 0; i < canvas.getWidth(); i+=110)
            {
                int firstBrickLayerY = 10;
                BreakoutBrick brick = new BreakoutBrick(getContext(), i, firstBrickLayerY + (x * 60), 100, 50);
                gameObjects.add(brick);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // Handle touch event actions
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                // Checks if the user clicked on the joystick
                if(joystick.isPressed(event.getX(), event.getY()))
                    joystick.setIsPressed(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                // Adjusts the joystick actuator positions based on the current mouse position
                if(joystick.getIsPressed())
                    joystick.setActuator(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_UP:
                // Resets the joystick pressed status and actuator values
                joystick.setIsPressed(false);
                joystick.resetActuator();

                // If the game is over this code will allow the
                // user to press the menu buttons that appear
                if (!isRunning)
                {
                    float x = event.getX();
                    float y = event.getY();
                    GameOver g = gameOverText;
                    if(x > g.retryLeft && x < g.retryRight && y > g.retryTop && y < g.retryBottom)
                        main.restart();
                    else if(x > g.backLeft && x < g.backRight && y > g.backTop && y < g.backBottom)
                        main.finishActivity();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    // Starts the GameLoop when the surface is created
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) { endGame(); }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { gameLoop.startLoop(); }

    // Ends the game if the surface is destroyed to avoid crashes
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    // Updates the position and other values of all game elements. Called by the gameLoop.
    @Override
    public void update(Canvas canvas) {
        joystick.update();
        player.update(joystick);
        ball.update();

        // If size == 1; All bricks have been removed, the 1 left over is the player object
        if(gameObjects.size() == 1)
        {
            // Increases level
            level++;
            // If the number of brick layers is below the maximum then increase
            if(brickLayers < MAX_BRICK_LAYERS)
                brickLayers++;
            // Re-initialize all the brick objects
            initBricks(canvas);

            // Increases the balls speed for every level
            ball.increaseSpeed();
        }
    }

    // Draw method that gets called by the gameLoop
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        joystick.draw(canvas);
        ball.draw(canvas);
        drawScore(canvas);

        // Draws each item in the gameObjects list
        for(int i = 0; i < gameObjects.size(); i++)
            gameObjects.get(i).draw(canvas);

        if(!isRunning) {
            // Displays the game over screen if the game is stopped
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
        canvas.drawText("SCORE: " + score, (canvas.getWidth()/2f + 150), canvas.getHeight() - 155, paint);
        canvas.drawText("LEVEL: " + level, (canvas.getWidth()/2f + 150), canvas.getHeight() - 80, paint);
    }

    // Removes object from gameObjects list
    public void removeObject(GameObject rect) {
        gameObjects.remove(rect);
    }

    // Stop updating the game
    @Override
    public void endGame() { gameLoop.endLoop(); }

    @Override
    public void gameOver() { isRunning = false; }

    @Override
    public void addScore(int i) { this.score += i; }

    @Override
    public double getScore() { return score; }
}
