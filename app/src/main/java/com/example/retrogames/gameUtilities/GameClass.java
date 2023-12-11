package com.example.retrogames.gameUtilities;

import android.graphics.Canvas;

public interface GameClass
{
    void initObjects(Canvas canvas);
    void draw(Canvas canvas);
    void update(Canvas canvas);
    void endGame();
    void gameOver();
    void removeObject(GameObject rect);
    void addScore(int i);
    double getScore();
}
