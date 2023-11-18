package com.example.retrogames;

import android.view.View;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public class CustomGameList extends ArrayAdapter
{
    private String[] gameNames;
    private String[] highScoreStrings;
    private Integer[] images;
    private Activity context;

    public CustomGameList(Activity context, String[] gameNames, String[] highScoreStrings, Integer[] images)
    {
        super(context, R.layout.row_item, gameNames);
        this.context = context;
        this.gameNames = gameNames;
        this.highScoreStrings = highScoreStrings;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
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
