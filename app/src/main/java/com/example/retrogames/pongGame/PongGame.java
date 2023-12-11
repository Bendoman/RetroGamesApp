package com.example.retrogames.pongGame;

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
import com.example.retrogames.gameUtilities.GameOver;
import com.example.retrogames.gameUtilities.MovablePaddle;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;
import com.example.retrogames.gameUtilities.Joystick;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class PongGame extends SurfaceView implements SurfaceHolder.Callback, GameClass
{
    private int score = 0;
    private int level = 1;
    private boolean isRunning = true;

    // Some breakout objects can be re-used here
    private PongBall ball;
    private Joystick joystick1;
    private Joystick joystick2;
    public GameOver gameOverText;
    private MovablePaddle player1;
    private MovablePaddle player2;
    private List<GameObject> gameObjects;

    private final GameLoop gameLoop;
    private final PongMainActivity main;

    public PongGame(Context context, PongMainActivity main)
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

        player1 = new MovablePaddle(getContext(), 500, canvas.getHeight() - 300, 250, 50, gameLoop.maxUPS);
        player2 = new MovablePaddle(getContext(), 500, 250, 250, 50, gameLoop.maxUPS);

        joystick1 = new Joystick(getContext(), canvas.getWidth()/2, canvas.getHeight() - 150, 70, 40);
        joystick2 = new Joystick(getContext(), canvas.getWidth()/2, 150, 70, 40);

        gameObjects.add(player1);
        gameObjects.add(player2);
        ball = new PongBall(getContext(), this, gameObjects, 500, 500, 25, 60);

        gameOverText = new GameOver(canvas, getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // Handle touch event actions
        // Functionally the same as the breakout game logic just doubled, one for each joystick
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // Checks if the user clicked on the joystick
                if(joystick1.isPressed((double) event.getX(), (double) event.getY()))
                    joystick1.setIsPressed(true);
                if(joystick2.isPressed((double) event.getX(), (double) event.getY()))
                    joystick2.setIsPressed(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                // Adjusts the joystick actuator positions based on the current mouse position
                if(joystick1.getIsPressed())
                    joystick1.setActuator((double) event.getX(), (double) event.getY());
                if(joystick2.getIsPressed())
                    joystick2.setActuator((double) event.getX(), (double) event.getY());
                return true;
            case MotionEvent.ACTION_UP:
                // Resets the joystick pressed status and actuator values
                joystick1.setIsPressed(false);
                joystick1.resetActuator();
                joystick2.setIsPressed(false);
                joystick2.resetActuator();

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
    public void surfaceCreated(@NonNull SurfaceHolder holder) { gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    // Ends the game if the surface is destroyed to avoid crashes
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) { endGame(); }

    // Updates the position and other values of all game elements. Called by the gameLoop.
    @Override
    public void update(Canvas canvas)
    {
        joystick1.update();
        joystick2.update();
        player1.update(joystick1);
        player2.update(joystick2);
        ball.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        player1.draw(canvas);
        player2.draw(canvas);
        joystick1.draw(canvas);
        joystick2.draw(canvas);
        ball.draw(canvas);
        drawScore(canvas);

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
        canvas.drawText("SCORE: " + score, (canvas.getWidth()/2f + 150), canvas.getHeight() - 170, paint);
        canvas.drawText("LEVEL: " + level, (canvas.getWidth()/2f + 150), canvas.getHeight() - 85, paint);
    }

    @Override
    public void removeObject(GameObject rect) { gameObjects.remove(rect); }

    // Stop updating the game
    @Override
    public void endGame() { gameLoop.endLoop(); }

    @Override
    public void gameOver() {
        isRunning = false;
    }

    // Adds the passed score and decreases the size of the paddles every five levels
    @Override
    public void addScore(int i) {
        this.score += i;
        if(score % 5 == 0)
        {
            level++;
            if(level < 5) {
                player1.reduceSize();
                player2.reduceSize();
            }
        }

    }

    @Override
    public double getScore() { return this.score; }
}
