package com.hrw.audiolibrary;

import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/04/28 14:30
 * @desc:
 */
public class AudioPlayHelper {
    MediaPlayer mediaPlayer;
    List<BaseAudioBean> audioBeans = new ArrayList<>();

    int currentAudio = 0;

    private AudioPlayHelper() {
        mediaPlayer = new MediaPlayer();


    }

    public static AudioPlayHelper create() {
        return new AudioPlayHelper();
    }

    public void setAudioDates(List<BaseAudioBean> dates) {
        audioBeans = dates;
    }

    public void setAudioData(BaseAudioBean data) {
        audioBeans.clear();
        audioBeans.add(data);
    }

    public void addAudioData(BaseAudioBean data) {
        audioBeans.add(data);
    }

    public void start() {
        mediaPlayer.start();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void start(String path) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();                   // 准备
            mediaPlayer.start();                        // 启动
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mediaPlayer.isPlaying()) {
//                    setPlayMode();
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentPlayPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    public void preAudio() {

    }

    public void nextAudio() {

    }


}
