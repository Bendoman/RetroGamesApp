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
import androidx.room.Room;

import com.example.retrogames.database.DAOs.UserDAO;
import com.example.retrogames.database.UserDatabase;
import com.example.retrogames.database.entities.User;

public class GamesListActivity extends AppCompatActivity
{
    private String username;
    public String getUsername() {
        return this.username;
    }
    private ListView listView;
    private TextView header;

    private User user;

    private String gameNames[] = { "Snake", "Breakout", "Tilter", "Pong" };
    private String userHighScoreStrings[] = { " ", " ", " ", " " };
    private String globalHighScoreStrings[] = { " ", " ", " ", " " };
    private String descriptions[] = { "placeholder", "placeholder", "placeholder", "placeholder" };
    private Integer images[] = { R.drawable.snake, R.drawable.breakout, R.drawable.tilter, R.drawable.pong};

    public UserDAO userDAO;
    public double snakeHighScore;
    public double breakoutHighScore;
    public double tilterHighScore;
    public double pongHighScore;

//    public double[] userHighScores = { snakeHighScore, breakoutHighScore, tilterHighScore, pongHighScore };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        // Database setup
        userDAO = Room.databaseBuilder(this, UserDatabase.class, "user-database")
                .allowMainThreadQueries().build().getUserDAO();

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        user =userDAO.getUserByName(username);
        loadList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GamesListActivity activity = GamesListActivity.this;
                Intent intent = new Intent(activity, GameInfo.class);

                user.setTilter_high_score(user.getTilter_high_score() + 100);

                String game = activity.gameNames[i - 1];
                String description  = activity.descriptions[i - 1];

                Bundle b = new Bundle();
                b.putInt("image", i - 1);
                b.putString("game", game);
                b.putString("description", description);
                b.putString("globalHighScore", activity.globalHighScoreStrings[i - 1]);
                b.putString("userHighScore", activity.userHighScoreStrings[i - 1]);

                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        loadList();
    }

    private void populateScoreData(String username)
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
        snakeHighScore = userDAO.getUserSnakeHighScore(username);
        breakoutHighScore = userDAO.getUserBreakoutHighScore(username);
        tilterHighScore = userDAO.getUserTilterHighScore(username);
        pongHighScore = userDAO.getUserPongHighScore(username);

        userHighScoreStrings[0] = Double.toString(snakeHighScore);
        userHighScoreStrings[1] = Double.toString(breakoutHighScore);
        userHighScoreStrings[2] = Double.toString(tilterHighScore);
        userHighScoreStrings[3] = Double.toString(pongHighScore);
    }

    private void loadList()
    {
        // Setting header
        header = new TextView(this);
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setText("Username: " + username + "   |  List of Games");

        populateScoreData(username);

        listView = (ListView) findViewById(R.id.games_list);
        listView.addHeaderView(header);

        CustomGameList gameList = new CustomGameList(this, gameNames, userHighScoreStrings, images);
        listView.setAdapter(gameList);
    }
}
