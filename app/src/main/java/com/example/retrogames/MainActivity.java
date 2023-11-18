package com.example.retrogames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private String username = "Anonymous";
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }
    public UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database setup
        userDAO = Room.databaseBuilder(this, UserDatabase.class, "user-database")
                .allowMainThreadQueries().build().getUserDAO();

        // Button to set username
        Button userNameButton = (Button) findViewById(R.id.userNameButton);
        EditText userNameField = (EditText) findViewById(R.id.userNameField);
        userNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = userNameField.getText().toString().toUpperCase();
                MainActivity.this.setUsername(username);
                Toast.makeText(MainActivity.this, "Username set to " + MainActivity.this.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });

        // Button to start games list activity
        Button listButton = (Button) findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = MainActivity.this.username;
                UserDAO DAO = MainActivity.this.userDAO;
                List<User> users = DAO.loadAllUsers();
                Log.d("DEBUG", users.toString());

                Boolean dbContatinsName = false;
                for(int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUser_name().equals(name)) {
                        dbContatinsName = true;
                        break;
                    }
                }
                if(!dbContatinsName)
                {
                    User user = new User();
                    user.setUser_name(name);

                    // TODO remove manual setting of high score here
                    // Placeholder values for testing purposes
                    user.setSnake_high_score(3000);

                    DAO.insertUser(user);
                }

                Intent intent = new Intent(MainActivity.this, GamesListActivity.class);
                intent.putExtra("username", MainActivity.this.getUsername());
                startActivity(intent);
            }
        });
    }
}
