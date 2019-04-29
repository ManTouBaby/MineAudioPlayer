package com.hrw.audiolibrary;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/04/28 14:35
 * @desc:
 */
public abstract class BaseAudioBean {


    private String audioPath;
    private String audioName;
    private int audioLength;

    public int getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(int audioLength) {
        this.audioLength = audioLength;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    protected abstract String setAudioName();

    protected abstract String setAudioPath();
    protected abstract int setAudioLength();

    public void initAudioData(){
        audioName = setAudioName();
        audioPath = setAudioPath();
        audioLength = setAudioLength();
    }
}
