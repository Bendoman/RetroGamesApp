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

    private int snake_high_score = 0;

    // Getters and setters
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getSnake_high_score() {
        return snake_high_score;
    }

    public void setSnake_high_score(int snake_high_score) {
        this.snake_high_score = snake_high_score;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
