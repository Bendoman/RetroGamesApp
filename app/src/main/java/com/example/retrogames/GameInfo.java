package com.example.retrogames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrogames.gameActivities.SnakeActivity;

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
        TextView gameuserHighScore = (TextView) findViewById(R.id.userHighScoreField);
        ImageView imageView = (ImageView) findViewById(R.id.gameImageView);

        // Get values from bundle
        Bundle b = getIntent().getExtras();
        int imageID = b.getInt("image");
        String gameName = b.getString("game");
        String description = b.getString("description");
        String globalHighScore = b.getString("globalHighScore");
        String userHighScore = b.getString("userHighScore");

        // Set values to views
        gameNameView.setText(gameName);
        gameDescriptionView.setText(description);
        gameGlobalHighScore.setText(globalHighScore);
        gameuserHighScore.setText(userHighScore);
        imageView.setImageResource(images[imageID]);

        // Start game activity
        Button playGame = (Button) findViewById(R.id.playGame);
        playGame.setOnClickListener(new View.OnClickListener()
        {
            // TODO Make this button start any of the game activities that are pressed not just snake
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GameInfo.this, SnakeActivity.class);
                startActivity(intent);
            }
        });
    }
}