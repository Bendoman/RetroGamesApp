package com.example.retrogames.gameActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.retrogames.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeActivity extends AppCompatActivity implements SurfaceHolder.Callback
{
    private final List<SnakeSegment> snakeSegments = new ArrayList<>();

    // For detecting swipe gesture
    private float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 75;

    // Surface holder used to draw
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private TextView scoreView;

    // right left up down
    private String direction = "right";

    private int score = 0;
    private static int snakeSpeed = 600;
    private static final int segmentSize = 40;
    private static final int defaultSnakeLength = 2;

    private int randomXPosition, randomYPosition;
    private int fruitPositionX, fruitPositionY;

    // Timer for game loop
    private Timer timer;
    private Canvas canvas = null;
    private Paint segmentColor = null;

    // SurfaceView width and height
    int surfaceWidth;
    int surfaceHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        scoreView = (TextView) findViewById(R.id.score);

        // Initializing image buttons
        final AppCompatImageButton up_arrow = (AppCompatImageButton) findViewById(R.id.up_arrow);
        final AppCompatImageButton down_arrow = (AppCompatImageButton) findViewById(R.id.down_arrow);
        final AppCompatImageButton left_arrow = (AppCompatImageButton) findViewById(R.id.left_arrow);
        final AppCompatImageButton right_arrow = (AppCompatImageButton) findViewById(R.id.right_arrow);

        // Adding callback to SurfaceView
        surfaceView.getHolder().addCallback((SurfaceHolder.Callback) this);

        segmentColor = new Paint();
        segmentColor.setColor(Color.GREEN);
        segmentColor.setStyle(Paint.Style.FILL);
        segmentColor.setAntiAlias(true);

        up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!direction.equals("down"))
                    direction = "up";
            }
        });

        down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!direction.equals("up"))
                    direction = "down";
            }
        });

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!direction.equals("right"))
                    direction = "left";
            }
        });

        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!direction.equals("left"))
                    direction = "right";
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
        this.surfaceHolder = surfaceHolder;

        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    private void init()
    {
        surfaceWidth = surfaceView.getWidth();
        surfaceHeight = surfaceView.getHeight();
        System.out.println(Integer.toString(surfaceWidth) + " : " + Integer.toString(surfaceHeight));

        snakeSegments.clear();
        scoreView.setText("0");
        score = 0;
        direction = "right";

        int startPosX = 360;

        for(int i = 0; i < defaultSnakeLength; i++)
        {
            SnakeSegment snakeSegment = new SnakeSegment(startPosX, 360);
            snakeSegments.add(snakeSegment);

            startPosX = startPosX - segmentSize;
        }

        addFruit();

        moveSnake();
    }

    private void addFruit() {
        randomXPosition = new Random().nextInt((surfaceWidth - segmentSize) / segmentSize);
        randomYPosition = new Random().nextInt((surfaceHeight - segmentSize) / segmentSize);

//        if(randomXPosition % 2 != 0)
//            randomXPosition++;
//        if(randomYPosition % 2 != 0)
//            randomYPosition++;

        fruitPositionX = (segmentSize * randomXPosition);
        fruitPositionY = (segmentSize * randomYPosition);
    }

    private void growSnake() {
        score++;
        if(snakeSpeed < 1000)
            snakeSpeed += 40;

        int size = snakeSegments.size();
        SnakeSegment snakeSegment =
                new SnakeSegment(
                        snakeSegments.get(size - 1).getPositionX(),
                        snakeSegments.get(size - 1).getPositionY());
        snakeSegments.add(snakeSegment);
    }
    private void moveSnake() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int headPositionX = snakeSegments.get(0).getPositionX();
                int headPositionY = snakeSegments.get(0).getPositionY();

                if(headPositionX == fruitPositionX && headPositionY == fruitPositionY) {
                    growSnake();

                    addFruit();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scoreView.setText(Integer.toString(score));
                        }
                    });
                }

                if( headPositionX > (surfaceWidth - segmentSize))
                    snakeSegments.get(0).setPositionX(0);
                else if( headPositionX < 0 )
                    snakeSegments.get(0).setPositionX(surfaceWidth - segmentSize);

                if( headPositionY > (surfaceHeight - segmentSize))
                    snakeSegments.get(0).setPositionY(0);
                else if(headPositionY < 0)
                    snakeSegments.get(0).setPositionY(surfaceHeight - segmentSize);

                for(int i = snakeSegments.size() - 1; i > 0; i--)
                {
                    snakeSegments.get(i).setPositionX(snakeSegments.get(i - 1).getPositionX());
                    snakeSegments.get(i).setPositionY(snakeSegments.get(i - 1).getPositionY());
                }

                switch (direction) {
                    case "right":
                        snakeSegments.get(0).setPositionX(snakeSegments.get(0).getPositionX() + segmentSize);
                        break;
                    case "left":
                        snakeSegments.get(0).setPositionX(snakeSegments.get(0).getPositionX() - segmentSize);
                        break;
                    case "up":
                        snakeSegments.get(0).setPositionY(snakeSegments.get(0).getPositionY() - segmentSize);
                        break;
                    case "down":
                        snakeSegments.get(0).setPositionY(snakeSegments.get(0).getPositionY() + segmentSize);
                        break;
                }

                if(checkGameOver(headPositionX, headPositionY))
                {
                    timer.purge();
                    timer.cancel();

                    AlertDialog.Builder builder = new AlertDialog.Builder(SnakeActivity.this);
                    builder.setMessage("Score " + score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            init();
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }
                else
                {
                    canvas = surfaceHolder.lockCanvas();

                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    // This will be changed to include full movement logic in a much cleaner way
                    for(int i = 1; i < snakeSegments.size(); i++)
                    {
                        segmentColor.setColor(Color.GREEN);
                        canvas.drawRect(snakeSegments.get(i).getPositionX(),
                                        snakeSegments.get(i).getPositionY(),
                                   snakeSegments.get(i).getPositionX() + segmentSize,
                                 snakeSegments.get(i).getPositionY() + segmentSize,
                                        segmentColor);
                    }

                    segmentColor.setColor(Color.RED);
                    canvas.drawRect(fruitPositionX, fruitPositionY, fruitPositionX + segmentSize, fruitPositionY + segmentSize, segmentColor);

                    segmentColor.setColor(Color.YELLOW);
                    canvas.drawRect(snakeSegments.get(0).getPositionX(),
                            snakeSegments.get(0).getPositionY(),
                            snakeSegments.get(0).getPositionX() + segmentSize,
                            snakeSegments.get(0).getPositionY() + segmentSize,
                            segmentColor);

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }, 1000 - snakeSpeed, 1000 - snakeSpeed);
    }

    private boolean checkGameOver(int headPositionX, int headPositionY){
        boolean gameOver = false;
        return gameOver;
    }

    @Override
    public void onBackPressed()
    {
        timer.purge();
        timer.cancel();

        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                float deltaX = x1 - x2;
                float deltaY = y1 - y2;

                if (Math.abs(deltaX) > MIN_DISTANCE && direction != "left" && direction != "right")
                {
                    if(x2 < x1)
                        direction = "left";
                    else
                        direction = "right";
                }
                else if(Math.abs(deltaY) > MIN_DISTANCE && direction != "up" && direction != "down")
                {
                    if(y2 < y1)
                        direction = "up";
                    else
                        direction = "down";
                }

                break;
        }
        return super.onTouchEvent(event);
    }
}


