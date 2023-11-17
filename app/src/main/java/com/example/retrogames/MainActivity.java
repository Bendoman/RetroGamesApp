package com.example.retrogames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, GamesListActivity.class);
                intent.putExtra("username", MainActivity.this.getUsername());

                startActivity(intent);
            }
        });
    }
}
