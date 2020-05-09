package com.qtimes.utils.android;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import com.qtimes.utils.android.PluLog;

import java.io.File;
import java.io.IOException;

/**
 * Author: JackHou
 * Date: 2019/10/25.
 */
public class VideoUtils {
    private MediaRecorder mediaRecorder;
    private Context mContext;

    public VideoUtils(Context mContext) {
        this.mContext = mContext;
        mediaRecorder = new MediaRecorder();
    }

    /**
     * 初始化
     *
     * @param mCamera camera
     * @param path    保存路径
     */
    public void initMediaParam(Camera mCamera, String path) {
        final File myDir = new File(path);
        if (!myDir.mkdirs()) {
            PluLog.e("Make dir failed");
        }

        mediaRecorder.setCamera(mCamera);//使用camera的属性
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//导入Camera摄像头
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置音频设备
        //设置格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//设置视频编码
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置音频编码
        //设置保存路径
        mediaRecorder.setOutputFile(path + System.currentTimeMillis() + ".mp4");
    }

    /**
     * 开始录制视频
     */
    public void start() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(mContext, "视频录制中", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止录制视频
     */
    public void stop() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            Toast.makeText(mContext, "视频停止录制", Toast.LENGTH_LONG).show();
        }
    }
}
