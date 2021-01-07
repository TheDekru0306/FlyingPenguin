package com.example.flying_penguin.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.res.Resources;
import android.os.CountDownTimer;

import com.example.flying_penguin.Audio;
import com.example.flying_penguin.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player extends GameObject{

    public int effect = -1;
    private Audio iceShardSound;
    private Bitmap resiedImage, b;
    private JoyStick joystick;
    private List<IceShard> iceShards = new ArrayList<IceShard>();
    private int height, width, MAX_SPEED = 10, score = 0;
    private double velocityX = 0, velocityY = 0;
    private boolean inverse = false, hasHitSecondBonus = false;
    private Random random = new Random();
    public CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            reverseEffect();
        }
    };

    public CountDownTimer iceShardTimer = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            reverseEffect();
        }
    };

    public Player(Context context) {
        super(context);

        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        sizeX = 120;
        sizeY = 120;
        positionY = height / 2;
        b = BitmapFactory.decodeResource(getResources(), R.drawable.penguin);
        resiedImage = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

        iceShardSound = new Audio(context);
    }

    public void setJoystick(JoyStick joystick){
        this.joystick = joystick;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(resiedImage, (int)positionX, (int)positionY, null);
    }

    @Override
    public void update() {

        if(!inverse) {
            velocityX = joystick.getActuatorX() * MAX_SPEED;
            velocityY = joystick.getActuatorY() * MAX_SPEED;
        }
        else{
            velocityX = -joystick.getActuatorX() * MAX_SPEED;
            velocityY = -joystick.getActuatorY() * MAX_SPEED;
        }


        if(((positionX + velocityX) >= 0) && ((positionX + velocityX) <= ((width / 2) - width * 0.1))){
            positionX += velocityX;
        }
        if(((positionY + velocityY) > 0) && ((positionY + velocityY) <= (height - sizeY))){
            positionY += velocityY;
        }
    }

    public void displayIceShards(Canvas canvas){
        for(IceShard iceShard : this.iceShards){
            iceShard.draw(canvas);
        }
    }
    public void updateIceShards(){
        for(IceShard iceShard : this.iceShards){
            iceShard.update();
        }
    }

    public void checkIfShardHitEnemy(Enemy enemy){
        for(IceShard iceShard : this.iceShards){
            if(iceShard.detectGameObjectCollision(enemy)){
                iceShard.positionX = width + iceShard.getSizeX() + 50;
                enemy.respawnEnemy();
                this.score += 20;
            }
        }
    }

    public void addIceShard(IceShard iceShard){
        iceShard.positionX = this.positionX - this.sizeX / 2;
        iceShard.positionY = this.positionY;
        iceShardSound.playShootAudio();
        this.iceShards.add(iceShard);
    }

    public int getIceShardsCount(){
        return iceShards.size();
    }

    public void hasRequestedIceShard(){
        for (IceShard iceShard : iceShards) {
            if(iceShard.positionX > (width + iceShard.getSizeX())){
                iceShard.positionX = this.positionX - this.sizeX / 2;
                iceShard.positionY = this.positionY;
                break;
            }
        }
    }

    public int getScore(){
        return this.score;
    }

    public void bonuesEffect(int bonusEffect){

        this.effect = bonusEffect;

        switch(bonusEffect){
            case 0: //Speed
                MAX_SPEED += 5;
                break;
            case 1: //Slowness
                MAX_SPEED -= 5;
                break;
            case 2: //Invert
                this.inverse = true;
                break;
            case 3: // Bigger
                this.sizeX += 5 + random.nextInt(40);
                this.sizeY = this.sizeX;
                resiedImage = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);
                break;
            case 4: //Smaller
                this.sizeX +=  -random.nextInt(40) - 5;
                this.sizeY = this.sizeX;
                resiedImage = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);
                break;
            case 5: //Bonus ( +100 )
                this.score += 100;
                break;
        }

        this.countDownTimer.start();
    }

    public void reverseEffect(){

        switch(this.effect){
            case 0:
                MAX_SPEED -= 5;
                break;
            case 1:
                MAX_SPEED += 5;
                break;
            case 2:
                this.inverse = false;
                break;
            case 3:
            case 4:
                this.sizeX = 120;
                this.sizeY = 120;
                resiedImage = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);
                break;
            default:
                break;
        }

        this.effect = -1;
    }

}
