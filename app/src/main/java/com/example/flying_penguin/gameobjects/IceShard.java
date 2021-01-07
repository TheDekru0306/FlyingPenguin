package com.example.flying_penguin.gameobjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.flying_penguin.R;

import java.util.Random;

public class IceShard extends GameObject{

    private Bitmap resizedIceShardImage;
    public int speed = 8, width, height;
    public IceShard(Context context) {
        super(context);

        Bitmap iceShardImage = BitmapFactory.decodeResource(getResources(), R.drawable.ice_shard);
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels;

        sizeX = 80;
        sizeY = 80;

        resizedIceShardImage = Bitmap.createScaledBitmap(iceShardImage, sizeX, sizeY, false);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(resizedIceShardImage, (int)positionX, (int)positionY, null);
    }

    @Override
    public void update() {
        this.positionX += speed;
    }

    public boolean isOutOfFrame(){
        return this.positionX > (width + sizeX);
    }

}
