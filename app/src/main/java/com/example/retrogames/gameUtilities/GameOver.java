package com.example.retrogames.gameUtilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.example.retrogames.R;

public class GameOver
{
    // Menu box paints
    private final Paint paint;
    private final Paint rectPaint;
    private final Paint backgroundPaint;

    public final float mainTop;
    public final float mainLeft;
    public final float mainRight;
    public final float mainBottom;

    public int backX;
    public int backY;
    public final float backTop;
    public final float backLeft;
    public final float backRight;
    public final float backBottom;

    public int retryX;
    public int retryY;
    public final float retryTop;
    public final float retryLeft;
    public final float retryRight;
    public final float retryBottom;

    public int positionX;
    public int positionY;

    private final String retryText = "TRY AGAIN";
    private final String backText = "BACK TO MENU";

    public GameOver(Canvas canvas, Context context)
    {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(ContextCompat.getColor(context, R.color.white));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Sets the main game over container X and Y to be the center of the screen
        this.positionX = (canvas.getWidth() / 2);
        this.positionY = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        // Sets the Rect values of the main container based on the text size and width
        paint.setTextSize(75);
        int mainWidth = (int) paint.measureText(retryText);
        this.mainTop = positionY + paint.ascent() - 25;
        this.mainLeft = positionX - (mainWidth/2f) - 50;
        this.mainBottom = positionY + paint.descent() + 250;
        this.mainRight = (positionX - (mainWidth/2f)) + mainWidth + 50;

        // Sets the Rect values of the back and retry containers based on the text size and width
        paint.setTextSize(25);
        int retryWidth = (int) paint.measureText(retryText);
        this.retryX = positionX;
        this.retryY = positionY + 100;
        this.retryTop = retryY + paint.ascent() - 15;
        this.retryLeft = retryX - (retryWidth/2f) - 15;
        this.retryBottom = retryY + paint.descent() + 15;
        this.retryRight = (retryX - (retryWidth/2f)) + retryWidth + 15;

        int backWidth = (int) paint.measureText(backText);
        this.backX = positionX;
        this.backY = positionY + 200;
        this.backTop = backY + paint.ascent() - 15;
        this.backLeft = retryX - (backWidth/2f) - 15;
        this.backBottom = backY + paint.descent() + 15;
        this.backRight = (retryX - (backWidth/2f)) + backWidth + 15;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.game_over_background));
        backgroundPaint.setStyle(Paint.Style.FILL);

        rectPaint = new Paint();
        rectPaint.setColor(ContextCompat.getColor(context, R.color.game_over_text));
        rectPaint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas)
    {
        // Draws a containing rectangle for each text option
        canvas.drawRect(mainLeft, mainTop, mainRight, mainBottom, backgroundPaint);
        canvas.drawRect(retryLeft, retryTop, retryRight, retryBottom, rectPaint);
        canvas.drawRect(backLeft, backTop, backRight, backBottom, rectPaint);

        // Draws the text itself
        paint.setTextSize(75);
        canvas.drawText("GAME OVER", positionX, positionY, paint);

        paint.setTextSize(25);
        canvas.drawText(retryText, positionX, retryY, paint);
        canvas.drawText(backText, positionX, backY, paint);
    }
}
