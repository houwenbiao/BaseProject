package com.qtimes.wonly.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.app.AppConstant;
import com.qtimes.domain.dagger.qualifier.ContextLevel;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Author: JackHou
 * Date: 2017/12/18/
 */

public class Player {
    AssetFileDescriptor fd = null;
    private MediaPlayer mPlayer;
    private Context mContext;

    @Inject
    public Player(@ContextLevel(ContextLevel.APPLICATION) Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 重头开始播放
     */
    public void restart() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        PluLog.i("player reset");
        fd = mContext.getResources().openRawResourceFd(AppConstant.voices[AppConstant.VoiceType.DING_DING]);
        try {
            mPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            fd.close();
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mPlayer.isPlaying()) {
//            PluLog.i("player isPlaying");
            return;
        }
        mPlayer.start();
    }

    public void play() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        if (mPlayer.isPlaying()) {
//            PluLog.i("player isPlaying");
            return;
        }
        mPlayer.reset();
        fd = mContext.getResources().openRawResourceFd(AppConstant.voices[AppConstant.VoiceType.DING_DING]);
        try {
            mPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            fd.close();
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.start();
    }

    public void play(int source) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        mPlayer.setLooping(true);
        fd = mContext.getResources().openRawResourceFd(AppConstant.voices[source]);
        try {
            mPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            fd.close();
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mPlayer.isPlaying()) {
            return;
        }
        mPlayer.start();
    }

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    public void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
        }
    }
}
