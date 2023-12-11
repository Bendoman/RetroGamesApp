package com.example.retrogames.tilterGame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
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


@SuppressLint("ViewConstructor")
public class TilterGame extends SurfaceView implements SurfaceHolder.Callback, GameClass
{
    private TilterBall ball;
    private GameOver gameOverText;
    private final GameLoop gameLoop;
    private final TilterMainActivity main;
    private TilterPlayingField playingField;

    private float xAcceleration = 0;
    private float yAcceleration = 0;

    private int score = 0;
    private int level = 1;
    public boolean isRunning = true;

    public TilterGame(Context context, TilterMainActivity main)
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

    // Initialize game objects
    @Override
    public void initObjects(Canvas canvas)
    {
        playingField = new TilterPlayingField(canvas);
        ball = new TilterBall(getContext(), this,  canvas.getWidth()/2f,
                canvas.getHeight()/2f, 30, 60, playingField);
        gameOverText = new GameOver(canvas, getContext());
    }

    // Only for interfacing with the game over menu in this game
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
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

    // Called by the game main activity
    public void updateAcceleration(float deltaX, float deltaY) {
        if(deltaX < xAcceleration && xAcceleration > 0)
        {
            if(deltaX < 0)
                this.xAcceleration = deltaX;
        }
        else if(deltaX > xAcceleration && xAcceleration < 0)
        {
            if(deltaX > 0)
                this.xAcceleration = deltaX;
        }
        else
            this.xAcceleration = deltaX;

        if(deltaY < yAcceleration && yAcceleration > 0)
        {
            if(deltaY < 0)
                this.yAcceleration = deltaY;
        }
        else if(deltaY > yAcceleration && yAcceleration < 0)
        {
            if(deltaY > 0)
                this.yAcceleration = deltaY;
        }
        else
            this.yAcceleration = deltaY;
    }

    // Starts the GameLoop when the surface is created
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    // Ends the game if the surface is destroyed to avoid crashes
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) { endGame(); }

    // Calls the overridden ball update method that takes acceleration parameters
    @Override
    public void update(Canvas canvas) {
        ball.update(xAcceleration, yAcceleration);
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        ball.draw(canvas);
        drawScore(canvas);
        playingField.draw(canvas);

        if(!isRunning) {
            // Draws the game over menu if the game has stopped
            gameOverText.draw(canvas);
            endGame(); // Ends the game loop
        }
    }

    public void drawScore(Canvas canvas)
    {
        Paint paint = new Paint();
        String score = Integer.toString(this.score);
        String level = Integer.toString(this.level);

        paint.setColor(ContextCompat.getColor(getContext(), R.color.deep_magenta));
        paint.setTextSize(40);
        canvas.drawText("SCORE: " + score, playingField.positionX,
                playingField.positionY + playingField.playingFieldHeight + 65, paint);
        canvas.drawText("LEVEL: " + level, playingField.positionX,
                playingField.positionY + playingField.playingFieldHeight + 115, paint);
    }

    // Stop updating the game
    @Override
    public void endGame() {
        gameLoop.endLoop();
    }

    @Override
    public void gameOver() {
        isRunning = false;
    }

    // Adds the passed score and reduces the size of the playing field every two levels
    @Override
    public void addScore(int i) {
        score += i;
        if(score % 2 == 0) {
            level++;
            ball.reduceFieldSize();
        }
    }

    @Override
    public double getScore() { return score; }

    // Overridden for the GameClass implementation but never needed
    @Override
    public void removeObject(GameObject rect) {}
}
