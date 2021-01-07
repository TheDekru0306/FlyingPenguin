package com.example.flying_penguin.gameobjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.flying_penguin.R;

import java.util.Random;

public class Enemy extends GameObject{

    private Bitmap resizedEnemyImage, enemyImage;
    public int speed = 10, width, height;
    Random random;

    public Enemy(Context context) {
        super(context);

        enemyImage = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels;

        random = new Random();
        sizeX = random.nextInt(100) + 30;
        sizeY = random.nextInt(100) + 30;

        positionX = width + sizeX;
        positionY = random.nextInt(height - 20);

        resizedEnemyImage = Bitmap.createScaledBitmap(enemyImage, sizeX, sizeY, false);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(resizedEnemyImage, (int)positionX, (int)positionY, null);
    }

    @Override
    public void update() {
        if(positionX + sizeX < 0 ){
           respawnEnemy();
        }
        else {
            positionX -= speed;
        }
    }

    public void respawnEnemy(){
        sizeX = random.nextInt(100) + 30;
        sizeY = random.nextInt(100) + 30;
        resizedEnemyImage = Bitmap.createScaledBitmap(enemyImage, sizeX, sizeY, false);

        positionX = width + sizeX;
        positionY = random.nextInt(height - 20);
    }
}
