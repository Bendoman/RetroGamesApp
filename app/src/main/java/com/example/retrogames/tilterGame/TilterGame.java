package com.example.retrogames.tilterGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;
import com.example.retrogames.gameUtilities.GameOver;


public class TilterGame extends SurfaceView implements SurfaceHolder.Callback, GameClass, SensorEventListener {

    private int score;
    private GameLoop gameLoop;
    private TilterBall ball;
    private TilterPlayingField playingField;
    private float xAcceleration = 0;
    private float yAcceleration = 0;
    private int level = 1;
    public GameOver gameOverText;
    private boolean isRunning = true;


    public TilterGame(Context context) {
        super(context);

        // Get surface holder and callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder, 60);
        score = 0;



        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) { endGame(); }

    @Override
    public void onSensorChanged(SensorEvent event) {}
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void initObjects(Canvas canvas) {
        playingField = new TilterPlayingField(canvas);
        ball = new TilterBall(getContext(), this,  canvas.getWidth()/2,
                canvas.getHeight()/2, 30, 60, playingField);
        gameOverText = new GameOver(canvas);
    }

    @Override
    public void update(Canvas canvas) {
        ball.update(xAcceleration, yAcceleration);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        ball.draw(canvas);
        playingField.draw(canvas);
        drawScore(canvas);
        if(!isRunning)
        {
            gameOverText.draw(canvas);
            endGame();
        }
    }

    public void drawScore(Canvas canvas) {
        String score = Integer.toString(this.score);
        String level = Integer.toString(this.level);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Score: " + score, playingField.positionX,
                playingField.positionY + playingField.playingFieldHeight + 65, paint);
        canvas.drawText("Level: " + level, playingField.positionX,
                playingField.positionY + playingField.playingFieldHeight + 115, paint);
    }

    @Override
    public void endGame() {
        gameLoop.endLoop();
    }

    @Override
    public void gameOver() {
        isRunning = false;
    }

    @Override
    public void removeObject(GameObject rect) {}

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
}
