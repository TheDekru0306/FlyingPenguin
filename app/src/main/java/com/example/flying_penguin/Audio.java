package com.example.flying_penguin;

import android.content.Context;
import android.media.MediaPlayer;

public class Audio {
    private MediaPlayer player;
    public boolean hasFinished = false;
    private Context context;

    public Audio(Context context){
        this.context = context;
    }

    public void playGameAudio(){
        player = MediaPlayer.create(context, R.raw.sound);
        player.setLooping(true);
        player.start();
    }
    public void playGameOverAudio(){
        player = MediaPlayer.create(context, R.raw.death);
        player.setOnCompletionListener(mediaPlayer -> {
            this.hasFinished = true;
        });
        player.start();
    }
    public void playShootAudio(){
        player = MediaPlayer.create(context, R.raw.laser_shoot);
        player.start();
    }

    public void stop(){
        if(player != null){
            this.hasFinished = false;
            player.release();
        }
        player = null;
    }

}
