package com.example.retrogames.snakeGame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.example.retrogames.R;
import com.example.retrogames.breakoutGame.BreakoutGame;
import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;
import com.example.retrogames.gameUtilities.GameOver;

public class SnakeMainActivity extends Activity {

    private User user;
    public UserDAO userDAO;
    private String username;
    private SnakeGame game;
    private SoundPool soundPool;
    private int successSound;
    private int gameOverSound;

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

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        successSound = soundPool.load(this, R.raw.success, 1);
        gameOverSound = soundPool.load(this, R.raw.gameover, 1);

        game = new SnakeGame(this, this);
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
        if(game.getScore() > user.getSnake_high_score()) {
            user.setSnake_high_score(game.getScore());
            userDAO.updateUser(user);
        }
    }

    public void playSound(int i) {
        soundPool.play(successSound,1, 1, 0, 0, 1);
    }

    @Override
    protected void onDestroy() {
        soundPool.release();
        soundPool = null;
        super.onDestroy();
    }
}