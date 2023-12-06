package com.example.retrogames;

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
import com.example.retrogames.breakoutGame.SnakeActivity;
import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;

public class GameInfo extends AppCompatActivity
{
    // For detecting swipe gesture
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    TextView gameGlobalHighScore;
    TextView gameUserHighScore;

    String user_name;
    private User user;
    public UserDAO userDAO;

    // TODO remove this from here and have it passed dynamically
    private Integer images[] = { R.drawable.snake, R.drawable.breakout, R.drawable.tilter, R.drawable.pong};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Database setup
        userDAO = Room.databaseBuilder(this, UserDatabase.class, "user-database")
                .allowMainThreadQueries().build().getUserDAO();


        // Get views from layout
        TextView gameNameView = (TextView) findViewById(R.id.gameNameField);
        TextView gameDescriptionView = (TextView) findViewById(R.id.gameDescriptionField);
        ImageView imageView = (ImageView) findViewById(R.id.gameImageView);

        gameGlobalHighScore = (TextView) findViewById(R.id.globalHighScoreField);
        gameUserHighScore = (TextView) findViewById(R.id.userHighScoreField);

        // Get values from bundle
        Bundle b = getIntent().getExtras();
        int imageID = b.getInt("image");
        String gameName = b.getString("game");
        String description = b.getString("description");
        String globalHighScore = b.getString("globalHighScore");
        String userHighScore = b.getString("userHighScore");
        user_name = b.getString("user_name");

        // Set values to views
        gameNameView.setText(gameName);
        gameDescriptionView.setText(description);
        imageView.setImageResource(images[imageID]);

        // Set scores
        loadScores();

        // Start game activity
        Button playGame = (Button) findViewById(R.id.playGame);
        playGame.setOnClickListener(new View.OnClickListener()
        {
            // TODO Make this button start any of the game activities that are pressed not just snake
            @Override
            public void onClick(View view)
            {
                Intent intent;
                switch(gameName) {
                    case "Snake":
                        intent = new Intent(GameInfo.this, SnakeActivity.class);
                        startActivity(intent);
                        break;
                    case "Breakout":
                        intent = new Intent(GameInfo.this, BreakoutMainActivity.class);
                        intent.putExtra("username", user_name);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x1 - x2;
                if (Math.abs(deltaX) > MIN_DISTANCE && x2 < x1 )
                    this.onBackPressed();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScores();
    }

    private void loadScores() {
        user = userDAO.getUserByName(user_name);
        String globalHighScore = Double.toString(userDAO.getGlobalBreakoutHighScore());
        String userHighScore = Double.toString(user.getBreakout_high_score());

        gameGlobalHighScore.setText(globalHighScore);
        gameUserHighScore.setText(userHighScore);
    }
}
