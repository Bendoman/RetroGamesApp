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

    // Surface holder used to draw
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private TextView scoreView;

    // right left up down
    private String direction = "right";

    private int score = 0;
    private static final int snakeSpeed = 700;
    private static final int segmentSize = 75;
    private static final int defaultSnakeLength = 3;

    private int randomXPosition, randomYPosition;
    private int fruitPositionX, fruitPositionY;

    // Timer for game loop
    private Timer timer;
    private Canvas canvas = null;
    private Paint segmentColor = null;

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
        snakeSegments.clear();
        scoreView.setText("0");
        score = 0;
        direction = "right";

        int startPosX = 200;

        for(int i = 0; i < defaultSnakeLength; i++)
        {
            SnakeSegment snakeSegment = new SnakeSegment(startPosX, 500);
            snakeSegments.add(snakeSegment);

            startPosX = startPosX - segmentSize;
        }

        addFruit();

        moveSnake();
    }

    private void addFruit() {
        // SurfaceView width and height
        int surfaceWidth = surfaceView.getWidth() - segmentSize;
        int surfaceHeight = surfaceView.getWidth() - segmentSize;

        randomXPosition = new Random().nextInt(surfaceWidth/segmentSize);
        randomYPosition = new Random().nextInt(surfaceHeight/segmentSize);

        if(randomXPosition % 2 != 0)
            randomXPosition++;

        if(randomYPosition % 2 != 0)
            randomYPosition++;

        fruitPositionX = (segmentSize * randomXPosition) + segmentSize;
        fruitPositionY = (segmentSize * randomYPosition) + segmentSize;
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
                    canvas.drawRect(snakeSegments.get(0).getPositionX(), snakeSegments.get(0).getPositionY(), snakeSegments.get(0).getPositionX() + segmentSize, snakeSegments.get(0).getPositionY() + segmentSize, segmentColor);

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }, 1000 - snakeSpeed, 1000 - snakeSpeed);
    }

    private void growSnake() {
        score++;
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
}

