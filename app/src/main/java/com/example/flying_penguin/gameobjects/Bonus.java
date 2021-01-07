package com.example.flying_penguin.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.flying_penguin.R;

import java.util.Random;

public class Bonus  extends GameObject{

    private int speed = 10;
    private Bitmap resiedImage , b;
    private Random random;


    public Bonus(Context context) {
        super(context);

        random = new Random();
        sizeX = 50;
        sizeY = 50;

        positionX = screenWidth + sizeX;
        positionY = random.nextInt(screenHeight - 20);

        b = BitmapFactory.decodeResource(getResources(), R.drawable.bonus);
        resiedImage = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(resiedImage, (int)positionX, (int)positionY, null);
    }

    @Override
    public void update() {
        if(positionX + sizeX < 0 ){
            respawnBonus();
        }
        else {
            positionX -= speed;
        }
    }

    public void respawnBonus(){
        positionX = screenWidth + sizeX;
        positionY = random.nextInt(screenHeight - 20);
    }

    public int getRandomBonus(){
        return random.nextInt(6);
    }
}
