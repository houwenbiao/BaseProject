package com.qtimes.wonly.events;

/**
 * Author: JackHou
 * Date: 2020/5/9.
 */
public class ApkInstallEvent {
    private long downLoadId;

    public ApkInstallEvent(long mDownLoadId) {
        downLoadId = mDownLoadId;
    }

    public long getDownLoadId() {
        return downLoadId;
    }

    public void setDownLoadId(long mDownLoadId) {
        downLoadId = mDownLoadId;
    }
}
