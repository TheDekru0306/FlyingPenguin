package com.example.flying_penguin.gameobjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;

public abstract class GameObject extends View {
    protected double positionX, positionY;
    protected int sizeX, sizeY, screenWidth, screenHeight;

    public GameObject(Context context) {
        super(context);
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public void setPosition(int positionX, int positionY){
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setSize(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }
    public abstract void update();

    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }

    public double getSizeX() { return sizeX; }
    public double getSizeY() { return sizeY; }

    public void setPositionX(double position) { this.positionX = position; }
    public void setPositionY(double position) { this.positionY = position; }

    public boolean detectGameObjectCollision(GameObject object) {
        for (int enemyY = 0; enemyY <= object.getSizeY(); enemyY++) {
            for (int enemyX = 0; enemyX <= object.getSizeX(); enemyX++) {
                if (((object.positionX + enemyX) - (this.positionX + this.sizeX / 2))
                        * ((object.positionX + enemyX) - (this.positionX + this.sizeX / 2))
                        + ((object.positionY + enemyY) - (this.positionY + this.sizeY / 2))
                        * ((object.positionY + enemyY) - (this.positionY + this.sizeY / 2)) <= (this.sizeX / 2
                        * this.sizeX / 2)) {
                    return true;
                }
            }
        }
        return false;
    }

}
