package com.example.retrogames.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.retrogames.database.entities.User;

import java.util.List;

/**
 * User data access object used for sending queries to the room database
 */
@Dao
public interface UserDAO
{
    // User queries
    @Delete
    public void deleteUser(User user);
    @Insert
    public void insertUser(User user);
    @Update
    public void updateUser(User user);
    @Query("Select * FROM User")
    public List<User> loadAllUsers();
    @Query("Select * FROM User WHERE user_name = :name")
    public User getUserByName(String name);

    // Global high scores
    @Query("Select MAX(snake_high_score) FROM User")
    public double getGlobalSnakeHighScore();
    @Query("Select MAX(breakout_high_score) FROM User")
    public double getGlobalBreakoutHighScore();
    @Query("Select MAX(tilter_high_score) FROM User")
    public double getGlobalTilterHighScore();
    @Query("Select MAX(pong_high_score) FROM User")
    public double getGlobalPongHighScore();
}
