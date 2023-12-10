package com.example.retrogames.pongGame;

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

public class PongMainActivity extends Activity {

    private User user;
    public UserDAO userDAO;
    private String username;
    private PongGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pong_game);

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

        game = new PongGame(this, this);
        setContentView(game);
    }

    public void restart() {
        updateScores();
        recreate();
    }
    public void finishActivity() {
        updateScores();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("score", game.getScore());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        updateScores();
        finishActivity();
    }

    public void updateScores() {
        if(game.getScore() > user.getPong_high_score()) {
            user.setPong_high_score(game.getScore());
            userDAO.updateUser(user);
        }
    }
}