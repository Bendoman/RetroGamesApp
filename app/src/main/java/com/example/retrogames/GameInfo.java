package com.example.retrogames;

import static com.example.retrogames.R.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrogames.breakoutGame.BreakoutMainActivity;
import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;
import com.example.retrogames.pongGame.PongMainActivity;
import com.example.retrogames.snakeGame.SnakeMainActivity;
import com.example.retrogames.tilterGame.TilterMainActivity;

public class GameInfo extends AppCompatActivity
{
    // For detecting swipe gesture
    private float x1;
    static final int MIN_DISTANCE = 150;

    TextView globalHighScoreTextView;
    TextView userHighScoreTextView;

    String user_name;
    public UserDAO userDAO;

    private String gameName;
    private String userHighScore;
    private String globalHighScore;
    private TextView lastScoreTextView;
    private final Integer[] images = { drawable.snake, drawable.breakout, drawable.tilter, drawable.pong};

    // Takes the result from finished game activities and updates the last score field based on it
    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();

                            if (result == RESULT_OK) {
                                lastScoreTextView.setText(Double.toString(data.getDoubleExtra("score", 0)));
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_game_info);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Database setup
        userDAO = Room.databaseBuilder(this, UserDatabase.class, "user-database")
                .allowMainThreadQueries().build().getUserDAO();

        // Get views from layout
        TextView gameNameView = (TextView) findViewById(id.gameNameField);
        ImageView imageView = (ImageView) findViewById(id.gameImageView);

        lastScoreTextView = (TextView) findViewById(id.lastScoreField);
        globalHighScoreTextView = (TextView) findViewById(id.globalHighScoreField);
        userHighScoreTextView = (TextView) findViewById(id.userHighScoreField);

        // Get values from bundle
        Bundle b = getIntent().getExtras();
        int imageID = b.getInt("image");
        gameName = b.getString("game");
        user_name = b.getString("user_name");

        // Set values to views
        gameNameView.setText(gameName);
        lastScoreTextView.setText("0");
        imageView.setImageResource(images[imageID]);

        // Set scores
        loadScores();

        // Start game activity
        Button playGame = (Button) findViewById(id.playGame);
        playGame.setOnClickListener(new View.OnClickListener()
        {
            // Detects clicks on each game tile and passes the name of the current user
            @Override
            public void onClick(View view)
            {
                Intent intent;
                switch(gameName) {
                    case "Snake":
                        intent = new Intent(GameInfo.this, SnakeMainActivity.class);
                        intent.putExtra("username", user_name);
                        activityResultLauncher.launch(intent);
                        break;
                    case "Breakout":
                        intent = new Intent(GameInfo.this, BreakoutMainActivity.class);
                        intent.putExtra("username", user_name);
                        activityResultLauncher.launch(intent);
                        break;
                    case "Pong":
                        intent = new Intent(GameInfo.this, PongMainActivity.class);
                        intent.putExtra("username", user_name);
                        activityResultLauncher.launch(intent);
                        break;
                    case "Tilter":
                        intent = new Intent(GameInfo.this, TilterMainActivity.class);
                        intent.putExtra("username", user_name);
                        activityResultLauncher.launch(intent);
                        break;
                }
            }
        });

        // Finishes the activity on pressing the return button
        Button backToMenu = (Button) findViewById(id.backToMenu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { finish(); }
        });
    }

    // Swipe gesture to return to the previous activity
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x1 - x2;
                if (Math.abs(deltaX) > MIN_DISTANCE && x2 > x1 )
                    finish();
                break;
        }
        return super.onTouchEvent(event);
    }

    // Access the room database and updates the global and
    // personal high score fields based on the username
    private void loadScores()
    {
        User user = userDAO.getUserByName(user_name);
        switch(gameName) {
            case "Snake":
                globalHighScore = Double.toString(userDAO.getGlobalSnakeHighScore());
                userHighScore = Double.toString(user.getSnake_high_score());
                break;
            case "Breakout":
                globalHighScore = Double.toString(userDAO.getGlobalBreakoutHighScore());
                userHighScore = Double.toString(user.getBreakout_high_score());
                break;
            case "Pong":
                globalHighScore = Double.toString(userDAO.getGlobalPongHighScore());
                userHighScore = Double.toString(user.getPong_high_score());
                break;
            case "Tilter":
                globalHighScore = Double.toString(userDAO.getGlobalTilterHighScore());
                userHighScore = Double.toString(user.getTilter_high_score());
                break;
        }
        userHighScoreTextView.setText(userHighScore);
        globalHighScoreTextView.setText(globalHighScore);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScores();
    }
}
