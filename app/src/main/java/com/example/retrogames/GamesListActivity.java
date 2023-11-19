package com.example.retrogames;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.room.Room;

import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;

public class GamesListActivity extends AppCompatActivity
{
    private TextView header;
    private ListView listView;

    private String username;
    public String getUsername() {
        return this.username;
    }

    private User user;
    public UserDAO userDAO;
    private String userHighScoreStrings[] = { " ", " ", " ", " " };
    private String globalHighScoreStrings[] = { " ", " ", " ", " " };
    private String gameNames[] = { "Snake", "Breakout", "Tilter", "Pong" };
    private String descriptions[] = { "placeholder", "placeholder", "placeholder", "placeholder" };
    private Integer images[] = { R.drawable.snake, R.drawable.breakout, R.drawable.tilter, R.drawable.pong};

    public double snakeHighScore;
    public double breakoutHighScore;
    public double tilterHighScore;
    public double pongHighScore;

    // For detecting swipe gesture
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        // Database setup
        userDAO = Room.databaseBuilder(this, UserDatabase.class, "user-database")
                .allowMainThreadQueries().build().getUserDAO();

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        // Setting header
        header = new TextView(this);
        header.setTextSize(20);
        header.setGravity(Gravity.CENTER);
        header.setText("Username: " + username);
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setPadding(35, 35, 35, 35);
        header.setBackgroundColor(Color.parseColor("#5400C2C9"));

        // Initializing list View
        listView = (ListView) findViewById(R.id.games_list);
        listView.addHeaderView(header);

        user = userDAO.getUserByName(username);
        loadList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GamesListActivity activity = GamesListActivity.this;
                Intent intent = new Intent(activity, GameInfo.class);

                String game = activity.gameNames[i - 1];
                String description  = activity.descriptions[i - 1];

                // Example to demo population of scores will be removed in full version
                switch (game) {
                    case "Snake":
                        user.setSnake_high_score(user.getSnake_high_score() + 100);
                        break;
                    case "Breakout":
                        user.setBreakout_high_score(user.getBreakout_high_score() + 100);
                        break;
                    case "Tilter":
                        user.setTilter_high_score(user.getTilter_high_score() + 100);
                        break;
                    case "Pong":
                        user.setPong_high_score(user.getPong_high_score() + 100);
                        break;
                }

                userDAO.updateUser(user);

                Bundle b = new Bundle();
                b.putInt("image", i - 1);
                b.putString("game", game);
                b.putString("description", description);
                b.putString("user_name", activity.username);
                b.putString("globalHighScore", activity.globalHighScoreStrings[i - 1]);
                b.putString("userHighScore", activity.userHighScoreStrings[i - 1]);

                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        loadList();
    }

    private void populateScoreData()
    {
        // Populating global score data
        snakeHighScore = userDAO.getGlobalSnakeHighScore();
        breakoutHighScore = userDAO.getGlobalBreakoutHighScore();
        tilterHighScore = userDAO.getGlobalTilterHighScore();
        pongHighScore = userDAO.getGlobalPongHighScore();

        globalHighScoreStrings[0] = Double.toString(snakeHighScore);
        globalHighScoreStrings[1] = Double.toString(breakoutHighScore);
        globalHighScoreStrings[2] = Double.toString(tilterHighScore);
        globalHighScoreStrings[3] = Double.toString(pongHighScore);

        // Populating user score data
        snakeHighScore = user.getSnake_high_score();
        breakoutHighScore = user.getBreakout_high_score();
        tilterHighScore = user.getTilter_high_score();
        pongHighScore = user.getPong_high_score();

        userHighScoreStrings[0] = Double.toString(snakeHighScore);
        userHighScoreStrings[1] = Double.toString(breakoutHighScore);
        userHighScoreStrings[2] = Double.toString(tilterHighScore);
        userHighScoreStrings[3] = Double.toString(pongHighScore);
    }

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
                x2 = event.getX();
                float deltaX = x1 - x2;
                if (Math.abs(deltaX) > MIN_DISTANCE && x2 < x1 )
                    this.onBackPressed();
                break;
        }
        return super.onTouchEvent(event);
    }
}

