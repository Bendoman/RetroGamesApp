package com.example.retrogames.breakoutGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.room.Room;

import com.example.retrogames.R;
import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;

public class BreakoutMainActivity extends Activity {

    private User user;
    public UserDAO userDAO;
    private String username;
    private BreakoutGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakout_main);

        // Database setup
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        userDAO = Room.databaseBuilder(this, UserDatabase.class, "user-database")
                .allowMainThreadQueries().build().getUserDAO();
        user = userDAO.getUserByName(username);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        game = new BreakoutGame(this, this);
        setContentView(game);
    }

    public void restart() {
        // So that any high scores are kept even if the user restarts
        updateScores();

        // Re-creates the activity instead of finishing and creating a new one
        // so that the responseListener still functions in the info activity.
        recreate();
    }

    public void finishActivity() {
        // Updating the score in the database
        updateScores();

        // Returns the current score from the game to be added to the last score field
        Intent returnIntent = new Intent();
        returnIntent.putExtra("score", game.getScore());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Updating the score in the database
        updateScores();
        // Finishes the activity so that the last score field gets filled out in the info page
        finishActivity();
    }

    public void updateScores() {
        // If the current game score is greater than the user's high score, update the field
        if(game.getScore() > user.getBreakout_high_score()) {
            user.setBreakout_high_score(game.getScore());
            userDAO.updateUser(user);
        }
    }
}
