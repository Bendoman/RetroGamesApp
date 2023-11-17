package com.example.retrogames;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameInfo extends AppCompatActivity
{
    // TODO remove this from here and have it passed dynamically
    private Integer images[] = { R.drawable.snake, R.drawable.breakout, R.drawable.tilter, R.drawable.pong};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        // Get views from layout
        TextView gameNameView = (TextView) findViewById(R.id.gameNameField);
        TextView gameDescriptionView = (TextView) findViewById(R.id.gameDescriptionField);
        TextView gameGlobalHighScore = (TextView) findViewById(R.id.globalHighScoreField);
        TextView gamePersonalHighScore = (TextView) findViewById(R.id.personalHighScoreField);
        ImageView imageView = (ImageView) findViewById(R.id.gameImageView);

        // Get values from bundle
        Bundle b = getIntent().getExtras();
        int imageID = b.getInt("image");
        String gameName = b.getString("game");
        String description = b.getString("description");
        String globalHighScore = b.getString("globalHighScore");
        String personalHighScore = b.getString("personalHighScore");

        // Set values to views
        gameNameView.setText(gameName);
        gameDescriptionView.setText(description);
        gameGlobalHighScore.setText(globalHighScore);
        gamePersonalHighScore.setText(personalHighScore);
        imageView.setImageResource(images[imageID]);

    }
}