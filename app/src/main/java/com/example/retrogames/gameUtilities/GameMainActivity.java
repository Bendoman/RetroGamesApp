package com.example.retrogames.gameUtilities;

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

import java.util.Objects;

public class GameMainActivity extends Activity {
    protected User user;
    protected UserDAO userDAO;
    protected GameClass game;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakout_main);

        // Database setup
        Intent intent = getIntent();
        String username = Objects.requireNonNull(intent.getExtras()).getString("username");

        userDAO = Room.databaseBuilder(this, UserDatabase.class, "user-database")
                .allowMainThreadQueries().build().getUserDAO();
        user = userDAO.getUserByName(username);

        // Sets the window to fullscreen
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }

    // Allows the user to restart the game.
    // Updates database scores and recreates the activity so that the
    // responseListener for the info activity still functions
    public void restart() { updateScores(); recreate(); }

    public void finishActivity()
    {
        // Updating the score in the database
        updateScores();

        // Returns the current score from the game to be added to the last score field
        Intent returnIntent = new Intent();
        returnIntent.putExtra("score", game.getScore());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    // Updates the score in the database and finishes the activity
    // so that the last score field gets filled out in the info page
    @Override
    public void onBackPressed() { updateScores(); finishActivity(); }

    // Updates the database
    public void updateScores() {} // Left empty and will be overridden

}
