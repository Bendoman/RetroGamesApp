package com.example.retrogames;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;

/**
 * Class displaying list of available games and the user high scores associate with them.
 *
 */
public class GamesListActivity extends AppCompatActivity
{
    private User user;
    public UserDAO userDAO;

    private String username;
    private ListView listView;
    private final String[] userHighScoreStrings = { " ", " ", " ", " " };
    private final String[] gameNames = { "Snake", "Breakout", "Tilter", "Pong" };
    private final Integer[] images = { R.drawable.snake, R.drawable.breakout, R.drawable.tilter, R.drawable.pong};

    // For detecting swipe gesture
    private float x1;
    static final int MIN_DISTANCE = 150;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_activity);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Database setup
        userDAO = Room.databaseBuilder(this, UserDatabase.class, "user-database")
                .allowMainThreadQueries().build().getUserDAO();

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        // Initializing list View
        listView = (ListView) findViewById(R.id.games_list);

        // Setting header
        TextView header = new TextView(this);
        header.setTextSize(20);
        header.setTextColor(Color.WHITE);
        header.setGravity(Gravity.CENTER);
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setText("USERNAME: " + username.toUpperCase());
        header.setPadding(35, 35, 35, 35);
        header.setBackgroundColor(Color.parseColor("#DE004E"));
        listView.addHeaderView(header);

        // Setting footer
        Button footerButton = new Button(this);
        footerButton.setTextSize(20);
        footerButton.setText("RETURN");
        footerButton.setTextColor(Color.WHITE);
        footerButton.setTypeface(Typeface.DEFAULT_BOLD);
        footerButton.setBackgroundColor(Color.parseColor("#DE004E"));
        listView.addFooterView(footerButton);

        user = userDAO.getUserByName(username);
        loadList();

        // Finishes the activity on footer return button click
        footerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // When a list item is clicked launch the game associated with that item
        // and pass the game name string, username and image id as extras
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GamesListActivity activity = GamesListActivity.this;
                Intent intent = new Intent(activity, GameInfo.class);

                String game = activity.gameNames[i - 1];

                Bundle b = new Bundle();
                b.putInt("image", i - 1);
                b.putString("game", game);
                b.putString("user_name", activity.username);

                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    // Reloads the scores list on resume so it gets updated after returning from a game
    @Override
    protected void onResume() { super.onResume(); loadList(); }

    private void populateScoreData()
    {
        // Populating user score data
        user = userDAO.getUserByName(username);
        userHighScoreStrings[0] = Double.toString(user.getSnake_high_score());
        userHighScoreStrings[1] = Double.toString(user.getBreakout_high_score());
        userHighScoreStrings[2] = Double.toString(user.getTilter_high_score());
        userHighScoreStrings[3] = Double.toString(user.getPong_high_score());
    }

    // Updates the user high score values and recreates the listView adapter
    private void loadList()
    {
        populateScoreData();

        CustomGameList gameList = new CustomGameList(this, gameNames, userHighScoreStrings, images);
        listView.setAdapter(gameList);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x1 - x2;
                if (Math.abs(deltaX) > MIN_DISTANCE && x2 < x1 )
                    this.onBackPressed();
                break;
        }
        return super.onTouchEvent(event);
    }
}
