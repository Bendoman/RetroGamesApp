package com.example.retrogames.breakoutGame;

import android.content.Context;

import com.example.retrogames.gameUtilities.BouncingBall;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameObject;

import java.util.List;

public class BreakoutBall extends BouncingBall {

    public BreakoutBall(Context context, GameClass game, List<GameObject> gameObjects, double positionX, double positionY, int radius, double maxUPS) {
        super(context, game, gameObjects, positionX, positionY, radius, maxUPS);
    }

    @Override
    public void update() {
        // Checks if the ball is colliding with the edges of the screen and reverses its velocity if so
        if(positionX - radius <= 0 || positionX + radius >= canvasWidth)
            velocityX = -velocityX;
        if(positionY - radius <= 0 || positionY + radius >= canvasHeight)
            velocityY = -velocityY;

        // This for loop is for detecting collisions with the game objects ( Bricks and paddle )
        for(int i = 0; i < gameObjects.size(); i++)
        {
            GameObject rect = gameObjects.get(i);
            if(i == 0 && (positionY + radius) > (rect.getPositionY() + rect.getHeight() + 100))
                game.endGame();

            if(intersects(rect))
            {
                if (positionX < rect.getPositionX() || positionX > rect.getPositionX()+rect.getLength())
                    velocityX = -velocityX;
                velocityY = -velocityY;

                if(i > 0) {
                    game.removeObject(rect);
                    game.addScore(1);
                }
            }
        }

        positionX += velocityX;
        positionY += velocityY;
    }
}
