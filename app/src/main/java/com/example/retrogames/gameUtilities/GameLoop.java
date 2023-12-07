package com.example.retrogames.gameUtilities;

import android.view.SurfaceHolder;
import android.graphics.Canvas;


public class GameLoop extends Thread {
    private GameClass game;
    private SurfaceHolder surfaceHolder;

    private boolean isRunning = false;
    private boolean objectsInitialised = false;

    private double averageFPS;
    private double averageUPS;

    public double maxUPS;
    public double upsPeriod;

    public GameLoop(GameClass game, SurfaceHolder surfaceHolder, double maxUPS) {

        this.maxUPS = maxUPS;
        this.upsPeriod = 1E+3/maxUPS;
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public void setUPS(double UPS)
    {
        this.maxUPS = UPS;
        this.upsPeriod = 1E+3/maxUPS;
    }
    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    public void endLoop() {
        isRunning = false;
    }

    @Override
    public void run() {
        super.run();

        // Time and cycle count variables
        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while(isRunning)
        {
            // Update and render game
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if(!objectsInitialised)
                    {
                        game.initObjects(canvas);
                        objectsInitialised = true;
                    }

                    game.update(canvas);
                    updateCount++;

                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if(canvas != null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Pause game loop to not exceed target UPS
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount* upsPeriod - elapsedTime);
            if(sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Skip frames to keep up with target UPS
            while(sleepTime < 0 && updateCount < maxUPS - 1)
            {
                game.update(canvas);
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount* upsPeriod - elapsedTime);
            }

            // Calculate average UPS and FPS
            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000) {
                averageUPS = updateCount / (1E-3 * elapsedTime);
                averageFPS = frameCount / (1E-3 * elapsedTime);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
}
