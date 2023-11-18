package com.example.retrogames.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User
{
    @NonNull
    @PrimaryKey (autoGenerate = true)
    private int uid;

    @NonNull
    @ColumnInfo(name = "user_name")
    private String user_name;

    private double snake_high_score = 0;
    private double breakout_high_score = 0;
    private double tilter_high_score = 0;
    private double pong_high_score = 0;

    // Getters and setters
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public double getBreakout_high_score() {
        return breakout_high_score;
    }

    public void setBreakout_high_score(double breakout_high_score) {
        this.breakout_high_score = breakout_high_score;
    }

    public double getPong_high_score() {
        return pong_high_score;
    }

    public void setPong_high_score(double pong_high_score) {
        this.pong_high_score = pong_high_score;
    }

    public double getTilter_high_score() {
        return tilter_high_score;
    }

    public void setTilter_high_score(double tilter_high_score) {
        this.tilter_high_score = tilter_high_score;
    }

    public double getSnake_high_score() {
        return snake_high_score;
    }

    public void setSnake_high_score(double snake_high_score) {
        this.snake_high_score = snake_high_score;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
