package com.example.retrogames.snakeGame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.retrogames.R;
import com.example.retrogames.breakoutGame.BreakoutGame;
import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;

public class SnakeMainActivity extends Activity {

    private User user;
    public UserDAO userDAO;
    private String username;
    private SnakeGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_main);

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

        game = new SnakeGame(this);
        setContentView(game);
    }

    @Override
    public void onBackPressed() {
        if(game.getScore() > user.getSnake_high_score()) {
            user.setSnake_high_score(game.getScore());
            userDAO.updateUser(user);
        }
        super.onBackPressed();
    }
}