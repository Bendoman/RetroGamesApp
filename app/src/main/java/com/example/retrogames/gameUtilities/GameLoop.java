package com.example.retrogames.gameUtilities;

import android.view.SurfaceHolder;
import android.graphics.Canvas;

/**
 * Game loop class that follows best standard android game development practices.
 * Written and expanded upon with reference to the game loop implementation in this project
 * <a href="https://github.com/bukkalexander/AndroidStudio2DGameDevelopment">Reference</a>
 */
public class GameLoop extends Thread
{
    private final GameClass game;
    private final SurfaceHolder surfaceHolder;

    public double maxUPS;
    public double upsPeriod;

    private boolean isRunning = false;
    private boolean objectsInitialised = false;

    public GameLoop(GameClass game, SurfaceHolder surfaceHolder, double maxUPS)
    {
        this.game = game;
        // Different games need to run at different speeds
        this.maxUPS = maxUPS;
        this.upsPeriod = 1E+3/maxUPS;
        this.surfaceHolder = surfaceHolder;
    }

    // Allows the game speed to be changed
    // Snake updates the UPS after each level to increase the speed of the snake
    public void setUPS(double UPS)
    {
        this.maxUPS = UPS;
        this.upsPeriod = 1E+3/maxUPS;
    }

    // Sets the isRunning boolean to true and starts the SurfaceHolder thread
    // Is called by the game class when the surface is created
    public void startLoop() { isRunning = true; start(); }

    public void endLoop() {
        isRunning = false;
    }

    @Override
    public void run()
    {
        super.run();

        // Time and cycle count variables
        long sleepTime;
        long startTime;
        long elapsedTime;
        int updateCount = 0;

        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while(isRunning)
        {
            // Update and render game
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    // On the first update tick of the game calls the initObjects method in the game
                    if(!objectsInitialised)
                    {
                        game.initObjects(canvas);
                        objectsInitialised = true;
                    }

                    // Updates and then draws to the canvas
                    game.update(canvas);
                    updateCount++;
                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            finally {
                // Draws to the surface holder by passing the updated canvas object
                if(canvas != null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Pause game loop to not exceed target UPS
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * upsPeriod - elapsedTime);
            if(sleepTime > 0)
            {
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

            // Resets the update count and start time every second
            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000)
            {
                updateCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
} // End reference
