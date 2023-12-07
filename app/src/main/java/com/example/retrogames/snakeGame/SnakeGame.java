package com.example.retrogames.snakeGame;


import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;

public class SnakeGame extends SurfaceView implements SurfaceHolder.Callback, GameClass {
    private int score;
    private GameLoop gameLoop;
    private Snake snake;

    public SnakeGame(Context context) {
        super(context);

        // Get surface holder and callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        score = 0;

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { Log.d(".java", "loop started()"); gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}

    @Override
    public void initObjects(Canvas canvas) {
        snake = new Snake();

    }

    @Override
    public void update(Canvas canvas) {
        snake.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        snake.draw();
    }

    @Override
    public void endGame() { }

    @Override
    public void removeObject(GameObject rect) { }

    @Override
    public void addScore(int i) { score += i; }
    @Override
    public double getScore() { return score; }
}
