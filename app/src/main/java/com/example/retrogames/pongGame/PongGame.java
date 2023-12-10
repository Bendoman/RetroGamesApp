package com.example.retrogames.pongGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.retrogames.R;
import com.example.retrogames.gameUtilities.BouncingBall;
import com.example.retrogames.breakoutGame.BreakoutGame;
import com.example.retrogames.gameUtilities.Constants;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameOver;
import com.example.retrogames.gameUtilities.MovablePaddle;
import com.example.retrogames.gameUtilities.GameLoop;
import com.example.retrogames.gameUtilities.GameObject;
import com.example.retrogames.gameUtilities.Joystick;

import java.util.ArrayList;
import java.util.List;

public class PongGame extends SurfaceView implements SurfaceHolder.Callback, GameClass {

    private int score;
    private int level = 1;
    private GameLoop gameLoop;

    // Breakout objects can be re-used here
    private PongBall ball;
    private Joystick joystick1;
    private Joystick joystick2;
    private MovablePaddle player1;
    private MovablePaddle player2;
    List<GameObject> gameObjects;
    public GameOver gameOverText;

    private boolean isRunning = true;
    private PongMainActivity main;

    public PongGame(Context context, PongMainActivity main) {
        super(context);
        this.main = main;

        // Get surface holder and callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder, 60);
        score = 0;

        setFocusable(true);
    }

    public void initObjects(Canvas canvas)
    {
        // Initialize game objects
        gameObjects = new ArrayList<GameObject>();
        joystick1 = new Joystick(getContext(), canvas.getWidth()/2, canvas.getHeight() - 150, 70, 40);
        player1 = new MovablePaddle(getContext(), 500, canvas.getHeight() - 300, 250, 50, gameLoop.maxUPS);

        joystick2 = new Joystick(getContext(), canvas.getWidth()/2, 150, 70, 40);
        player2 = new MovablePaddle(getContext(), 500, 250, 250, 50, gameLoop.maxUPS);

        gameObjects.add(player1);
        gameObjects.add(player2);

        ball = new PongBall(getContext(), this, gameObjects, 500, 500, 25, 60);
        gameOverText = new GameOver(canvas, getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle touch event actions
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick1.isPressed((double) event.getX(), (double) event.getY()))
                    joystick1.setIsPressed(true);
                if(joystick2.isPressed((double) event.getX(), (double) event.getY()))
                    joystick2.setIsPressed(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick1.getIsPressed())
                    joystick1.setActuator((double) event.getX(), (double) event.getY());
                if(joystick2.getIsPressed())
                    joystick2.setActuator((double) event.getX(), (double) event.getY());

                return true;
            case MotionEvent.ACTION_UP:
                joystick1.setIsPressed(false);
                joystick1.resetActuator();
                joystick2.setIsPressed(false);
                joystick2.resetActuator();

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

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) { endGame(); }

    public void drawScore(Canvas canvas) {
        String score = Integer.toString(this.score);
        String level = Integer.toString(this.level);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Score: " + score, (canvas.getWidth()/2 + 150), canvas.getHeight() - 125, paint);
        canvas.drawText("Level: " + level, (canvas.getWidth()/2 + 150), canvas.getHeight() - 75, paint);
    }

    @Override
    public void update(Canvas canvas) {
        joystick1.update();
        joystick2.update();
        player1.update(joystick1);
        player2.update(joystick2);
        ball.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        joystick1.draw(canvas);
        player1.draw(canvas);
        joystick2.draw(canvas);
        player2.draw(canvas);
        ball.draw(canvas);
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
    public void removeObject(GameObject rect) { gameObjects.remove(rect); }

    @Override
    public void addScore(int i) {
        main.playSound(Constants.BLOCK_HIT_SOUND);
        this.score += i;
    }
    @Override
    public double getScore() { return this.score; }
}
