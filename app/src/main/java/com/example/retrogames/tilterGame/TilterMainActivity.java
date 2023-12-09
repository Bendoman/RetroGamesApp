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
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.example.retrogames.R;
import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;
import com.example.retrogames.gameUtilities.GameOver;
import com.example.retrogames.pongGame.PongGame;

public class TilterMainActivity extends Activity implements SensorEventListener {

    private User user;
    public UserDAO userDAO;
    private String username;
    private TilterGame game;
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
    
        // Get a reference to SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        
        game = new TilterGame(this);
        setContentView(game);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                GameOver g = game.gameOverText;

                if(x > g.retryX && x < g.retryX + g.retryWidth && y > g.retryY && y < g.retryY + g.retryHeight)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("score", game.getScore());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }

                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if(game.getScore() > user.getTilter_high_score()) {
            user.setTilter_high_score(game.getScore());
            userDAO.updateUser(user);
        }
        super.onBackPressed();
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
}
