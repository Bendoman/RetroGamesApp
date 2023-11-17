package com.example.retrogames;


import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GamesListActivity extends AppCompatActivity
{
    private ListView listView;
    private TextView header;

    private String gameNames[] = { "Snake", "Breakout", "Tilter", "Pong" };
    private String highScoreStrings[] = { "placeholder", "placeholder", "placeholder", "placeholder" };
    private Integer images[] = { R.drawable.snake, R.drawable.breakout, R.drawable.tilter, R.drawable.pong};

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        // Setting header
        header = new TextView(this);
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setText("List of Games");

        listView = (ListView) findViewById(R.id.games_list);
        listView.addHeaderView(header);


        // Populating list data
        CustomGameList gameList = new CustomGameList(this, gameNames, highScoreStrings, images);
        listView.setAdapter(gameList);
    }
}
