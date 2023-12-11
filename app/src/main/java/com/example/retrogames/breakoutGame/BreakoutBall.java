package com.example.retrogames.breakoutGame;

import android.content.Context;

import com.example.retrogames.gameUtilities.BouncingBall;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameObject;

import java.util.List;

/**
 * Extends the bouncing ball class as the breakout game has its own collision logic that
 * needs to be implemented separately.
 */
public class BreakoutBall extends BouncingBall
{
    public BreakoutBall(Context context, GameClass game, List<GameObject> gameObjects,
                        double positionX, double positionY, int radius, double maxUPS) {
        super(context, game, gameObjects, positionX, positionY, radius, maxUPS);
    }

    /**
     * Overriding the update method to allow the ball to loop through a list of game objects
     * that will act as the breakout blocks in the game.
     */
    @Override
    public void update()
    {
        // As wall collision logic is the same for all games
        super.update();

        // This for loop is for detecting collisions with the game objects ( Bricks and paddle )
        for(int i = 0; i < gameObjects.size(); i++)
        {
            GameObject rect = gameObjects.get(i);
            /*
             * the 0th element in this list is the player paddle.
             * This if statement checks if the ball is below the
             * bottom of the paddle + 100 pixels which is the fail state for the game.
             */
            if(i == 0 && (positionY + radius) > (rect.getPositionY() + rect.getHeight() + 100))
                game.gameOver();

            if(intersects(rect))
            {
                velocityY = -velocityY;
                // If the ball is coming from the side, also reverse the x velocity
                if (positionX < rect.getPositionX() || positionX > rect.getPositionX()+rect.getLength())
                    velocityX = -velocityX;

                // i == 0 is the paddle, which should never be removed
                if(i > 0) {
                    game.removeObject(rect);
                    game.addScore(1);
                }

                // Fixes a bug where the ball gets stuck
                // within a moving paddles body if coming from the side
                if (rect.getVelocityX() != 0) {
                    positionY = rect.getPositionY() - radius - 1;
                }
            }
        }

        // Updates the position based on the velocity
        positionX += velocityX;
        positionY += velocityY;
    }
}
