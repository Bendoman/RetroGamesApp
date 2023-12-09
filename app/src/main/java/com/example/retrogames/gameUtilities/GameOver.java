package com.example.retrogames.gameUtilities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class GameOver {
    private final Paint paint;

    public int retryHeight;
    public int retryWidth;
    public int backWidth;
    public int backHeight;
    public int retryX;
    public int retryY;
    public int backX;
    public int backY;

    public int positionX;
    public int positionY;

    private final String retryText = "TRY AGAIN";
    private final String backText = "BACK TO MENU";

    public GameOver(Canvas canvas) {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.MAGENTA);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(75);

        this.positionX = (canvas.getWidth() / 2);
        this.positionY = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        Rect bounds = new Rect();
        paint.getTextBounds(retryText, 0, retryText.length(), bounds);
        this.retryWidth = bounds.width();
        this.retryHeight = bounds.height();

        this.retryX = positionX;
        this.retryY = (int) (positionY + 100 - ((paint.descent() + paint.ascent()) / 2));

        paint.getTextBounds(backText, 0, backText.length(), bounds);
        this.backWidth = bounds.width();
        this.backHeight = bounds.height();

        this.backX = positionX;
        this.backY = positionY + 200;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawText("GAME OVER", positionX, positionY, paint);

        paint.setTextSize(25);
        canvas.drawText(retryText, positionX, retryY, paint);
        canvas.drawText(backText, positionX, backY, paint);
    }
}

