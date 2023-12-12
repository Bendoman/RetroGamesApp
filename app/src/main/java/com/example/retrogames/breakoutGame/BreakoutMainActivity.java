package com.example.retrogames.breakoutGame;

import android.os.Bundle;

import com.example.retrogames.gameUtilities.GameMainActivity;

/**
 * The activity associated with the breakout game
 */
public class BreakoutMainActivity extends GameMainActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.game = new BreakoutGame(this, this);
        setContentView((BreakoutGame) this.game);
    }

    // Updates the database
    @Override
    public void updateScores()
    {
        // If the current game score is greater than the user's high score, update the field
        if(game.getScore() > user.getBreakout_high_score())
        {
            user.setBreakout_high_score(game.getScore());
            userDAO.updateUser(user);
        }
    }
}
