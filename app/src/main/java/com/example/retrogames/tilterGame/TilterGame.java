package com.example.retrogames.tilterGame;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;
import android.content.pm.ActivityInfo;


public class TilterGame extends SurfaceView implements SurfaceHolder.Callback, GameClass, SensorEventListener {

    private int score;
    private GameLoop gameLoop;
    private TilterBall ball;


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
    public void onSensorChanged(SensorEvent event) {

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void initObjects(Canvas canvas) {
        ball = new TilterBall(getContext(), this,  250, 50, 100, 60);
    }

    @Override
    public void update(Canvas canvas) {
//        ball.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        ball.draw(canvas);
    }

    @Override
    public void endGame() { gameLoop.endLoop(); }

    @Override
    public void removeObject(GameObject rect) {}

    @Override
    public void addScore(int i) { score += i; }
    @Override
    public double getScore() { return score; }

}
