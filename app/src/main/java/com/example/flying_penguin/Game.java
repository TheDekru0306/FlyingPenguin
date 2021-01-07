package com.example.flying_penguin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.flying_penguin.gameobjects.Background;
import com.example.flying_penguin.gameobjects.Bonus;
import com.example.flying_penguin.gameobjects.Enemy;
import com.example.flying_penguin.gameobjects.IceShard;
import com.example.flying_penguin.gameobjects.JoyStick;
import com.example.flying_penguin.gameobjects.Performance;
import com.example.flying_penguin.gameobjects.Player;
import com.example.flying_penguin.screens.GameOver;

import java.util.ArrayList;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    public boolean isRunning = true;
    private int joystickPointerId = 0;
    private GameLoop gameLoop;
    private GameOver gameOver;
    private Performance performance;
    private Background background;
    private Player player;
    private JoyStick joystick;
    private Audio playerAudio, iceShardSound;
    private List<Bonus> bonuses = new ArrayList<Bonus>();
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private Context context;
    private int width, height, timer = 0, iceShards = 0, maxIceShards = 5;
    private int hasRequestedIceShard = 0;

    //private SharedPreferences sp = getSharedPreferences("penguin", context.MODE_PRIVATE);

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameOver = new GameOver(context);
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;

        gameLoop = new GameLoop(this, surfaceHolder);
        performance = new Performance(context, gameLoop);
        player = new Player(context);

        background = new Background(context);
        int joystickX = (int) (width * 0.15), joystickY = (int) (height * 0.80);
        joystick = new JoyStick(joystickX, joystickY, 70, 40);

        player.setJoystick(joystick);
        this.context = context;
        playerAudio = new Audio(this.context);
        iceShardSound = new Audio(this.context);
        playerAudio.playGameAudio();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Handle user input touch event actions
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(!isRunning){
                    resetGame();
                    isRunning = true;
                }else{

                    if (joystick.getIsPressed()) {
                        // Joystick was pressed before this event -> cast spell
                        if(iceShards < maxIceShards && player.getIceShardsCount() < maxIceShards){
                            iceShards ++;
                        }
                        else{
                            if(hasRequestedIceShard < maxIceShards){
                                iceShardSound.playShootAudio();
                                hasRequestedIceShard++;
                            }
                        }
                    } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                        // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                        joystickPointerId = event.getPointerId(event.getActionIndex());
                        joystick.setIsPressed(true);
                    } else {
                        // Joystick was not previously, and is not pressed in this event -> cast spell
                        if(iceShards < maxIceShards && player.getIceShardsCount() < maxIceShards){
                            iceShardSound.playShootAudio();
                            iceShards ++;
                        }
                        else{
                            if(hasRequestedIceShard < maxIceShards){
                                hasRequestedIceShard++;
                            }
                        }
                    }
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    // Joystick was pressed previously and is now moved
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    // joystick pointer was let go off -> setIsPressed(false) and resetActuator()
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }

        return super.onTouchEvent(event);
    }


    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if(isRunning){
            background.draw(canvas);


            for (Enemy enemy : enemies) {
                enemy.draw(canvas);
            }

            for (Bonus bonus : bonuses) {
                bonus.draw(canvas);
            }

            player.draw(canvas);

            player.displayIceShards(canvas);

            joystick.draw(canvas);

            performance.draw(canvas);
        }
        else{
            gameOver.draw(canvas);
        }
    }

    public void update(){

        if(isRunning){

            timer++;
            background.update();

            if(timer >= 20){
                if(enemies.size() != 15){
                    enemies.add(new Enemy(this.context));
                }
                if(bonuses.size() != 4){
                   bonuses.add(new Bonus(this.context));
                }
                timer = 0;
            }

            player.update();
            player.updateIceShards();

            while(iceShards > 0){
                player.addIceShard(new IceShard(this.context));
                iceShards--;
            }
            while(hasRequestedIceShard > 0){
                player.hasRequestedIceShard();
                hasRequestedIceShard--;
            }

            for (Enemy enemy : enemies) {
                enemy.update();
                if(player.detectGameObjectCollision(enemy)){
                    playerAudio.stop();
                    gameOver.setScore(player.getScore());
                    gameOver.playAudio();
                    isRunning = false;
                    break;
                }
                else{
                    player.checkIfShardHitEnemy(enemy);
                }
            }

            for(Bonus bonus : bonuses){
                bonus.update();
                if(player.detectGameObjectCollision(bonus)){

                    bonus.respawnBonus();

                    if(player.effect == -1){
                        player.bonuesEffect(bonus.getRandomBonus());
                        player.countDownTimer.start();
                    }
                    else{
                        player.countDownTimer.cancel();
                        player.reverseEffect();
                        player.bonuesEffect(bonus.getRandomBonus());
                    }
                }
            }

            joystick.update();
        }
    }

    public void resetGame(){
        if(gameOver.isAudioPlaying()){
            gameOver.stopAudio();
        }
        this.player = new Player(this.context);
        this.player.setJoystick(this.joystick);
        this.bonuses = new ArrayList<Bonus>();
        this.enemies = new ArrayList<Enemy>();
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
