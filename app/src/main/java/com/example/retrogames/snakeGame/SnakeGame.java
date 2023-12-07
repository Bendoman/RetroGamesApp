package com.example.retrogames.snakeGame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;
import com.example.retrogames.gameUtilities.Joypad;
import com.example.retrogames.gameUtilities.Joystick;

public class SnakeGame extends SurfaceView implements SurfaceHolder.Callback, GameClass {
    private int score;
    private GameLoop gameLoop;
    private Snake snake;
    private Joypad joypad;
    private Snake snake2;
    private PlayingField playingField;

    public SnakeGame(Context context) {
        super(context);

        // Get surface holder and callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder, 2);
        score = 0;

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle touch event actions
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                joypad.calculateDirection((double) event.getX(), (double) event.getY());

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
        playingField = new PlayingField(canvas);

        snake = new Snake(getContext(), canvas, playingField, 50);
        snake2 = new Snake(getContext(), canvas, playingField, 50);
        joypad = new Joypad(canvas.getWidth()/2 - 50, canvas.getHeight() - 250);
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
        snake2.draw(canvas);
        playingField.draw(canvas);
    }

    @Override
    public void endGame() {
        // Stop updating the game
        gameLoop.endLoop();
    }

    @Override
    public void removeObject(GameObject rect) { }

    @Override
    public void addScore(int i) { score += i; }
    @Override
    public double getScore() { return score; }
}
