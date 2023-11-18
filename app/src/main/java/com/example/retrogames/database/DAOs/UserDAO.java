package com.example.retrogames.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.retrogames.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO
{
    @Insert
    public void insertUser(User user);

    @Update
    public void updateUser(User user);

    @Delete
    public void deleteUser(User user);

    @Query("Select * FROM User")
    public List<User> loadAllUsers();

    @Query("Select * FROM User WHERE user_name = :name")
    public User getUserByName(String name);

    @Query("Select MAX(snake_high_score) FROM User")
    public int getGlobalHighScore();
}
