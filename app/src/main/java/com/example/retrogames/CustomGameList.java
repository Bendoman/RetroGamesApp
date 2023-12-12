package com.example.retrogames;

import android.annotation.SuppressLint;
import android.view.View;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class CustomGameList extends ArrayAdapter
{
    private final Integer[] images;
    private final String[] gameNames;
    private final String[] highScoreStrings;

    private final Activity context;

    public CustomGameList(Activity context, String[] gameNames, String[] highScoreStrings, Integer[] images)
    {
        super(context, R.layout.row_item, gameNames);

        this.images = images;
        this.context = context;
        this.gameNames = gameNames;
        this.highScoreStrings = highScoreStrings;
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();

        if(convertView==null)
            row = inflater.inflate(R.layout.row_item, null, true);

        ImageView gameImageView = (ImageView) row.findViewById(R.id.gameImageView);
        TextView gameTextView = (TextView) row.findViewById(R.id.gameNameTextView);
        TextView highScoreTextView = (TextView) row.findViewById(R.id.highScoreTextView);

        gameTextView.setText(gameNames[position]);
        highScoreTextView.setText("Your high score: " + highScoreStrings[position]);
        gameImageView.setImageResource(images[position]);

        return row;
    }
}
