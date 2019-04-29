package com.hrw.audiolibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/04/28 14:44
 * @desc:
 */
public class AudioPlayView extends RelativeLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private SeekBar sbMusic;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private ImageView ivPlayLoopModel;
    private ImageView ivPlayPre;
    private ImageView ivPause;
    private ImageView ivPlayNext;
    private ImageView ivShowMusicList;

    AudioPlayHelper playHelper;
    int showModel = 0;//0、全部功能模式  1、带进度条播放模式 2、纯播放模式
    private boolean isStop;//是否停止播放

    public AudioPlayView(Context context) {
        this(context, null);
    }

    public AudioPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AudioPlayView);
        showModel = typedArray.getInt(R.styleable.AudioPlayView_showModel, 0);

        View view = null;
        switch (showModel) {
            case 0://全部功能
                view = LayoutInflater.from(context).inflate(R.layout.item_audio_player_view, null);
                ivPlayLoopModel = view.findViewById(R.id.iv_music_play_btn_loop);
                ivPlayPre = view.findViewById(R.id.iv_music_prev);
                ivPlayNext = view.findViewById(R.id.iv_music_next);
                ivShowMusicList = view.findViewById(R.id.iv_show_music_list);

                tvCurrentTime = view.findViewById(R.id.tv_music_current);
                tvTotalTime = view.findViewById(R.id.tv_music_total);
                sbMusic = view.findViewById(R.id.sb_music);

                ivPlayLoopModel.setOnClickListener(this);
                ivPlayPre.setOnClickListener(this);
                ivPlayNext.setOnClickListener(this);
                ivShowMusicList.setOnClickListener(this);
                sbMusic.setOnSeekBarChangeListener(this);
                break;
            case 1://存在播放进度条
                view = LayoutInflater.from(context).inflate(R.layout.item_audio_player_view1, null);
                tvCurrentTime = view.findViewById(R.id.tv_music_current);
                tvTotalTime = view.findViewById(R.id.tv_music_total);
                sbMusic = view.findViewById(R.id.sb_music);
                sbMusic.setOnSeekBarChangeListener(this);
                break;
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.item_audio_player_view2, null);
                break;

        }

        addView(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivPause = view.findViewById(R.id.iv_music_pause);
        ivPause.setOnClickListener(this);

        playHelper = AudioPlayHelper.create();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sbMusic.setProgress(msg.what);
            tvCurrentTime.setText(formatTime(msg.what));
        }
    };


    public void setPlayModelShow(boolean isShow) {
        ivPlayLoopModel.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setPlayListShow(boolean isShow) {
        ivShowMusicList.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void play(String path) {
        playHelper.start(path);
        ivPause.setImageResource(R.mipmap.ic_play_btn_pause);
    }

    public void play(BaseAudioBean audioBean){
        play(audioBean.getAudioPath());
        if (showModel==2)return;
        sbMusic.setProgress(0);
        sbMusic.setMax(audioBean.getAudioLength());
        tvTotalTime.setText(formatTime(audioBean.getAudioLength()));
        tvCurrentTime.setText("00:00");
        new Thread(new AudioThread()).start();
        isStop = false;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_music_play_btn_loop) {

        }
        if (i == R.id.iv_music_prev) {

        }
        if (i == R.id.iv_music_pause) {
            if (playHelper.isPlaying()) {
                playHelper.stop();
                isStop = true;
                ivPause.setImageResource(R.mipmap.ic_play_btn_next_pressed);
            } else {
                playHelper.start();
                ivPause.setImageResource(R.mipmap.ic_play_btn_pause);
            }
        }
        if (i == R.id.iv_music_next) {

        }
        if (i == R.id.iv_show_music_list) {

        }
    }

    //格式化数字
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            playHelper.seekTo(progress);
            tvCurrentTime.setText(formatTime(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    class AudioThread implements Runnable {

        @Override
        public void run() {
            while (!isStop) {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(playHelper.getCurrentPlayPosition());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
