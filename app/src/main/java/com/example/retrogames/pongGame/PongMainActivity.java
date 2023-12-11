package com.example.retrogames.pongGame;

import android.os.Bundle;
import com.example.retrogames.gameUtilities.GameMainActivity;

public class PongMainActivity extends GameMainActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.game = new PongGame(this, this);
        setContentView((PongGame) this.game);
    }

    // Updates the database
    @Override
    public void updateScores() {
        // If the current game score is greater than the user's high score, update the field
        if(game.getScore() > user.getPong_high_score()) {
            user.setPong_high_score(game.getScore());
            userDAO.updateUser(user);
        }
    }
}
