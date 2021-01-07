package com.example.flying_penguin.screens;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.flying_penguin.Audio;
import com.example.flying_penguin.R;

public class GameOver extends View {

    private int screenHeight, screenWidth, sizeX, sizeY, score;
    public boolean restart = false;
    private Bitmap resizedGameOverImage;
    private Paint paintRect, paintText;
    private Rect rect;
    private String scoreText;

    private Audio audioPlayer;

    public GameOver(Context context) {
        super(context);

        audioPlayer = new Audio(context);
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        paintRect = new Paint();
        paintText = new Paint();

        paintText.setColor(Color.RED);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize((int)(screenHeight * 0.05));
        paintText.setAntiAlias(true);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);
        rect = new Rect();

        sizeX = (int)(400 * 0.8);
        sizeY = (int)(400 * 0.8);

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
        resizedGameOverImage = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        paintRect.setColor(Color.WHITE);
        rect.set(0, 0, screenWidth, screenHeight);

        canvas.drawRect(rect, paintRect);
        canvas.drawBitmap(resizedGameOverImage, screenWidth / 2 - sizeX / 2, screenHeight / 2 - sizeY / 2, null);

        this.scoreText = "Score: " + Integer.toString(this.score);
        canvas.drawText(this.scoreText, screenWidth / 2, screenHeight / 2  + sizeY, paintText);
    }

    public void playAudio(){
        audioPlayer.playGameOverAudio();
    }

    public void update() {
        if(audioPlayer.hasFinished){
            audioPlayer.stop();
        }
    }

    public void setScore(int score){
        this.score = score;
    }

    public boolean isAudioPlaying(){
        return !audioPlayer.hasFinished;
    }

    public void stopAudio(){
        audioPlayer.stop();
    }
}
