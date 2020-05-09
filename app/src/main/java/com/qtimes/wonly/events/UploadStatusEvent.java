package com.qtimes.wonly.events;

import com.qtimes.wonly.delegate.UploadDelegate;

/**
 * Author: JackHou
 * Date: 2020/3/18.
 */
public class UploadStatusEvent {

    private UploadDelegate.UploadStatus status;

    public UploadStatusEvent(UploadDelegate.UploadStatus status) {
        this.status = status;
    }

    public UploadDelegate.UploadStatus getStatus() {
        return status;
    }

    public void setStatus(UploadDelegate.UploadStatus status) {
        this.status = status;
    }
}
