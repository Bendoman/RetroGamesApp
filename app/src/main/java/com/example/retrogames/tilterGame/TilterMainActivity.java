package com.example.retrogames.tilterGame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.example.retrogames.R;
import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;
import com.example.retrogames.gameUtilities.Constants;
import com.example.retrogames.gameUtilities.GameOver;
import com.example.retrogames.pongGame.PongGame;

public class TilterMainActivity extends Activity implements SensorEventListener {

    private User user;
    public UserDAO userDAO;
    private SoundPool soundPool;
    private String username;
    private TilterGame game;
    private int successSound;
    private int gameOverSound;
    private int gameStartSound;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilter_main);

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
        gameStartSound = soundPool.load(this, R.raw.gamestart, 1);

        // Get a reference to SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        
        game = new TilterGame(this, this);
        setContentView(game);
    }


    public void finishActivity() {
        updateScores();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("score", game.getScore());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void restart() {
        updateScores();
        recreate();
    }

    @Override
    public void onBackPressed() {
        updateScores();
        finishActivity();
    }

    public void updateScores() {
        if(game.getScore() > user.getTilter_high_score()) {
            user.setTilter_high_score(game.getScore());
            userDAO.updateUser(user);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            float xAcceleration = event.values[1];
            float yAcceleration = event.values[0];

            game.updateAcceleration(xAcceleration, yAcceleration);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        // Re-register on reload
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        // Unregister the listener so that the app doesn't crash
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public void playSound(int i) {
        int sound = -1;
        switch (i) {
            case Constants.SUCCESS_SOUND:
                sound = successSound;
                break;
            case Constants.GAME_OVER_SOUND:
                sound = gameOverSound;
                break;
        }
        if(sound != -1)
            soundPool.play(sound,1, 1, 0, 0, 1);
    }

    @Override
    protected void onDestroy() {
        soundPool.release();
        soundPool = null;
        super.onDestroy();
    }
}
