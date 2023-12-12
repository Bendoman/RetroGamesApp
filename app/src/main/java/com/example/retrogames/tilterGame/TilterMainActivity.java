package com.example.retrogames.tilterGame;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.retrogames.gameUtilities.GameMainActivity;

public class TilterMainActivity extends GameMainActivity implements SensorEventListener
{
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get a reference to SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);

        this.game = new TilterGame(this, this);
        setContentView((TilterGame) this.game);
    }

    // Updates the database
    public void updateScores() {
        // If the current game score is greater than the user's high score, update the field
        if(game.getScore() > user.getTilter_high_score()) {
            user.setTilter_high_score(game.getScore());
            userDAO.updateUser(user);
        }
    }

    // Takes the change in Gyroscope values and updates the acceleration values in the game
    // Implements the sensor listening logic here as the getSystemService
    // call used in the constructor is unavailable to non activity classes
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            float xAcceleration = event.values[1];
            float yAcceleration = event.values[0];

            // Casting to TilterGame as the other games do not implement updateAcceleration()
            TilterGame g = (TilterGame) game;
            g.updateAcceleration(xAcceleration, yAcceleration);
        }
    }

    // Unregister the listener on stop so that the app doesn't crash
    @Override
    protected void onStop()
    {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    // Re-register the listener on reload
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
    }

    // Unused but implemented from interface
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
