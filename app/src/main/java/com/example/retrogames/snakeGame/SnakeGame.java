package com.example.retrogames.snakeGame;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.Constants;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;
import com.example.retrogames.gameUtilities.GameOver;
import com.example.retrogames.gameUtilities.Joypad;

public class SnakeGame extends SurfaceView implements SurfaceHolder.Callback, GameClass {
    private static final float MIN_DISTANCE = 15;
    private final SnakeMainActivity main;
    private int score;
    private GameLoop gameLoop;
    private Snake snake;
    private Joypad joypad;
    private SnakePlayingField playingField;
    private int level = 1;
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public boolean isRunning = true;
    public GameOver gameOverText;

    public SnakeGame(Context context, SnakeMainActivity main) {
        super(context);
        this.main = main;

        // Get surface holder and callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder, 4);
        score = 0;

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle touch event actions
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                joypad.calculateDirection((double) event.getX(), (double) event.getY());
                // For detecting swipe gesture
                x1 = event.getX();
                y1 = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                // Dealing with swipe gesture
                x2 = event.getX();
                y2 = event.getY();

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
                // of direction.
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

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { Log.d(".java", "loop started()"); gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) { endGame(); }


    @Override
    public void initObjects(Canvas canvas) {
        playingField = new SnakePlayingField(canvas);

        snake = new Snake(getContext(), canvas, this, playingField, 50);
        joypad = new Joypad(canvas.getWidth()/2 - 50, canvas.getHeight() - 250);

        gameOverText = new GameOver(canvas, getContext());
    }

    @Override
    public void update(Canvas canvas) {
        joypad.update();
        snake.update(joypad);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        joypad.draw(canvas);
        snake.draw(canvas);
        playingField.draw(canvas);
        drawScore(canvas);
        if(!isRunning)
        {
            gameOverText.draw(canvas);
            endGame();
        }
    }

    @Override
    public void endGame() {
        // Stop updating the game
        gameLoop.endLoop();
    }

    @Override
    public void gameOver() {
        isRunning = false;
        main.playSound(Constants.GAME_OVER_SOUND);
    }

    @Override
    public void removeObject(GameObject rect) { }

    @Override
    public void addScore(int i) {
        main.playSound(Constants.SUCCESS_SOUND);
        score += i;
        if(gameLoop.maxUPS < 10 && score % 5 == 0) {
            gameLoop.setUPS(gameLoop.maxUPS + 0.5);
            level++; 
        }
    }
    @Override
    public double getScore() { return score; }

    public void drawScore(Canvas canvas) {
        String score = Integer.toString(this.score);
        String level = Integer.toString(this.level);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Score: " + score, (canvas.getWidth()/2 + 200), canvas.getHeight() - 115, paint);
        canvas.drawText("Level: " + level, (canvas.getWidth()/2 + 200), canvas.getHeight() - 65, paint);
    }
}
