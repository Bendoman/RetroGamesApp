package com.example.retrogames.gameUtilities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class GameOver {
    private final Paint paint;
    public final float retryTop;
    public final float retryBottom;
    public final float backTop;
    public final float backBottom;
    private final Paint rectPaint;
    public float backLeft;
    public float backRight;
    public float retryLeft;
    public float retryRight;

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


        this.positionX = (canvas.getWidth() / 2);
        this.positionY = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        paint.setTextSize(25);
        retryWidth = (int) paint.measureText(retryText);
        this.retryX = positionX;
        this.retryY = positionY + 100;

        this.retryLeft = retryX - (retryWidth/2) - 15;
        this.retryRight = (retryX - (retryWidth/2)) + retryWidth + 15;
        this.retryTop = retryY + paint.ascent() - 15;
        this.retryBottom = retryY + paint.descent() + 15;

        backWidth = (int) paint.measureText(backText);

        this.backX = positionX;
        this.backY = positionY + 200;

        this.backLeft = retryX - (backWidth/2) - 15;
        this.backRight = (retryX - (backWidth/2)) + backWidth + 15;
        this.backTop = backY + paint.ascent() - 15;
        this.backBottom = backY + paint.descent() + 15;

        rectPaint = new Paint();
        rectPaint.setColor(Color.argb(255, 0, 255, 255));
        rectPaint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(retryLeft, retryTop, retryRight, retryBottom, rectPaint);
        canvas.drawRect(backLeft, backTop, backRight, backBottom, rectPaint);

        paint.setTextSize(75);
        canvas.drawText("GAME OVER", positionX, positionY, paint);

        paint.setTextSize(25);
        canvas.drawText(retryText, positionX, retryY, paint);
        canvas.drawText(backText, positionX, backY, paint);
    }
}

