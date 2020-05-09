package com.qtimes.pavilion.events;

import com.qtimes.pavilion.enums.DownloadCode;
import com.qtimes.pavilion.enums.DownloadType;

/**
 * Author: JackHou
 * Date: 2020/2/12.
 */
public class ProgressEvent {
    public DownloadType type;
    public float progress;
    public int timeLeft;
    public int speed;
    public DownloadCode code;


    public ProgressEvent(DownloadType type, float progress, int timeLeft, int speed, DownloadCode code) {
        this.type = type;
        this.progress = progress;
        this.timeLeft = timeLeft;
        this.speed = speed;
        this.code = code;
    }

    @Override
    public String toString() {
        return "ProgressEvent{" +
                "type='" + type + '\'' +
                ", progress=" + progress +
                ", timeLeft=" + timeLeft +
                ", speed=" + speed +
                ", code=" + code +
                '}';
    }
}
