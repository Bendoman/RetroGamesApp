package com.example.retrogames.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.entities.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase
{
    public abstract UserDAO getUserDAO();
}
