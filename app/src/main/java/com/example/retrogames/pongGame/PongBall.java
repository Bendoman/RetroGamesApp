package com.example.retrogames.pongGame;

import android.content.Context;

import com.example.retrogames.gameUtilities.BouncingBall;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameObject;

import java.util.List;

public class PongBall extends BouncingBall
{
    public PongBall(Context context, GameClass game, List<GameObject> gameObjects,
                    double positionX, double positionY, int radius, double maxUPS) {
        super(context, game, gameObjects, positionX, positionY, radius, maxUPS);
    }

    @Override
    public void update()
    {
        // As wall collision logic is the same for all games
        super.update();

        // This for loop is for detecting collisions with the game objects ( Bricks and paddle )
        for(int i = 0; i < gameObjects.size(); i++)
        {
            GameObject rect = gameObjects.get(i);
            // If the ball is below the bottom paddle or above the top paddle the game is over
            if(canvasHeight != 0 && (positionY > canvasHeight - 100 || positionY < 100))
                game.gameOver();

            // Reverses X and Y velocity if the ball intersects a paddle
            if(intersects(rect))
            {
                // Increases score every time the ball bounces
                game.addScore(1);

                // This is for handling the case where the ball intersects with a paddle that has
                // velocity. It stops the ball from getting stuck inside the paddle
                if (rect.getVelocityX() != 0) // Rect is moving
                {
                    if(velocityY < 0)
                        positionY = rect.getPositionY() + rect.getHeight() + radius + 1;
                    else
                        positionY = rect.getPositionY() - radius - 1;
                }

                if (positionX < rect.getPositionX() || positionX > rect.getPositionX()+rect.getLength()) {
                    velocityX = -velocityX;
                }
                velocityY = -velocityY;
            }
        }

        positionX += velocityX;
        positionY += velocityY;
    }
}
