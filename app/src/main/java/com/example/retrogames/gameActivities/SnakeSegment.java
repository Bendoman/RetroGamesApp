package com.example.retrogames.gameActivities;

public class SnakeSegment
{
    private int positionX, positionY;

    public SnakeSegment(int positionX, int positionY)
    {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
}
