package com.example.retrogames.snakeGame;

import android.os.Bundle;

import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.entities.User;
import com.example.retrogames.gameUtilities.GameMainActivity;

/**
 * The activity associated with the snake game
 */
public class SnakeMainActivity extends GameMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.game = new SnakeGame(this, this);
        setContentView((SnakeGame) this.game);
    }

    // Updates the database
    @Override
    public void updateScores()
    {
        // If the current game score is greater than the user's high score, update the field
        if(game.getScore() > user.getBreakout_high_score())
        {
            user.setSnake_high_score(game.getScore());
            userDAO.updateUser(user);
        }
    }
}
