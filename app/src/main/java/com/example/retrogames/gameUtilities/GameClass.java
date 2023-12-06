package com.example.retrogames.gameUtilities;

import android.graphics.Canvas;
import android.widget.CalendarView;

public interface GameClass {
    public void initObjects(Canvas canvas);
    public void draw(Canvas canvas);
    public void update(Canvas canvas);
    public void endGame();

    public void removeObject(GameObject rect);

    public void addScore(int i);

    public double getScore();
}
