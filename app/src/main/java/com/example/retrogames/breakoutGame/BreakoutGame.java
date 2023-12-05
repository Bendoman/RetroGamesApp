package com.example.retrogames.breakoutGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

import java.util.ArrayList;
import java.util.List;

//Manages all objects in the game
public class BreakoutGame extends SurfaceView implements SurfaceHolder.Callback {
    private static final int MAX_BRICK_LAYERS = 10;
    private BreakoutPaddle player;
    private Joystick joystick;
    private GameLoop gameLoop;
    private Ball ball;

    private int brickLayers = 1;
    private int firstBrickLayerY = 10;

    private int score;

    List<GameObject> gameObjects;
    private int level;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle touch event actions
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystick.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()) {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;
        }

        return super.onTouchEvent(event);
    }

    public BreakoutGame(Context context) {
        super(context);

        // Get surface holder and callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        score = 0;

        setFocusable(true);
    }
    public void initObjects(Canvas canvas)
    {
        // Initialize game objects
        gameObjects = new ArrayList<GameObject>();
        joystick = new Joystick(canvas.getWidth()/2, canvas.getHeight()-140, 70, 40);
        player = new BreakoutPaddle(getContext(), 500, 1500, 250, 50);
        gameObjects.add(player);
        initBricks(canvas);

        ball = new Ball(getContext(), this, gameObjects, 500, 500, 25);
    }

    public void initBricks(Canvas canvas)
    {
        for (int x = 0; x < brickLayers; x ++) {
            for(int i = 0; i < canvas.getWidth(); i+=110)
            {
                GameBrick brick = new GameBrick(getContext(), i, firstBrickLayerY + (x * 60), 100, 50);
                gameObjects.add(brick);
            }
        }
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) { endGame(); }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        drawUPS(canvas);
//        drawFPS(canvas);
        drawScore(canvas);

        joystick.draw(canvas);
        ball.draw(canvas);
//        player.draw(canvas);

        for(int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).draw(canvas);
        }
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 50, paint);
    }

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
    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 150, paint);
    }

    public void update(Canvas canvas) {
        joystick.update();
        player.update(joystick);
        ball.update();

        // All bricks have been removed, the 1 left over is the player object
        if(gameObjects.size() == 1)
        {
            // Increases level
            level++;
            // If the number of brick layers is below the maximum then increase
            if(brickLayers < MAX_BRICK_LAYERS)
                brickLayers++;
            // Re-initialize all the brick objects
            initBricks(canvas);

            ball.increaseSpeed();
        }
    }

    public void removeObject(GameObject rect) {
        gameObjects.remove(rect);
    }

    public void endGame() {
        gameLoop.endLoop();
    }

    public void addScore(int i) {
        this.score += i;
    }
}
