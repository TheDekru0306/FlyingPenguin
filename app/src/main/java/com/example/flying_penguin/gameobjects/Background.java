package com.example.flying_penguin.gameobjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.example.flying_penguin.R;

public class Background extends View {

    Paint p;
    Bitmap resiedImage;
    private int width, height;
    private int positionX= 0, x2;

    public Background(Context context) {
        super(context);

        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        this.x2 = width;

        Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.background);
        resiedImage = Bitmap.createScaledBitmap(b, width, height, false);
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setPosition(int positionX){
        this.positionX = positionX;
    }

    public void update() {
        positionX -= 5;
        x2 -= 5;
        if (Math.abs(positionX) >= width) {
            positionX = width + (width - Math.abs(positionX));
        }
        if (Math.abs(x2) >= width) {
            x2 = width + (width - Math.abs(x2));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p=new Paint();

        canvas.drawBitmap(resiedImage, positionX, 0, p);
        canvas.drawBitmap(resiedImage, x2, 0, p);
    }

}
