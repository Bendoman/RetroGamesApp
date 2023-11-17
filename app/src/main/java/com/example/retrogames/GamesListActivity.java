package com.example.retrogames;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GamesListActivity extends AppCompatActivity
{
    private String username;
    public String getUsername() {
        return this.username;
    }
    private ListView listView;
    private TextView header;

    private String gameNames[] = { "Snake", "Breakout", "Tilter", "Pong" };
    private String highScoreStrings[] = { "placeholder", "placeholder", "placeholder", "placeholder" };
    private Integer images[] = { R.drawable.snake, R.drawable.breakout, R.drawable.tilter, R.drawable.pong};

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");

        // Setting header
        header = new TextView(this);
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setText("Username: " + username + "   |  List of Games");

        listView = (ListView) findViewById(R.id.games_list);
        listView.addHeaderView(header);

        // Populating list data
        CustomGameList gameList = new CustomGameList(this, gameNames, highScoreStrings, images);
        listView.setAdapter(gameList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GamesListActivity activity = GamesListActivity.this;
                Intent intent = new Intent(activity, GameInfo.class);

                String game = activity.gameNames[i - 1];
                String globalHighScore = activity.highScoreStrings[i - 1];
                // String personal high Score
                Bundle b = new Bundle();
                b.putInt("image", i - 1);
                b.putString("game", game);
                b.putString("globalHighScore", globalHighScore);
                b.putString("personalHighScore", "Placeholder");
                b.putString("description", "Test description");

                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
