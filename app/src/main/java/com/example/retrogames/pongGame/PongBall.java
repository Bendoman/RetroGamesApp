package com.example.retrogames.pongGame;

import android.content.Context;
import android.util.Log;

import com.example.retrogames.gameUtilities.BouncingBall;
import com.example.retrogames.gameUtilities.GameClass;
import com.example.retrogames.gameUtilities.GameObject;

import java.util.List;

public class PongBall extends BouncingBall {
    public PongBall(Context context, GameClass game, List<GameObject> gameObjects, double positionX, double positionY, int radius) {
        super(context, game, gameObjects, positionX, positionY, radius);
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
            if(canvasHeight != 0 && (positionY > canvasHeight - 100 || positionY < 100))
                game.endGame();

            if(intersects(rect))
            {
                game.addScore(1);

                if (positionX < rect.getPositionX() || positionX > rect.getPositionX()+rect.getLength())
                    velocityX = -velocityX;
                velocityY = -velocityY;
            }
        }

        positionX += velocityX;
        positionY += velocityY;
    }
}
