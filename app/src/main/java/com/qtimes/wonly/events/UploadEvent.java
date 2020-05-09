package com.qtimes.wonly.events;

/**
 * Author: JackHou
 * Date: 2019/12/21.
 */
public class UploadEvent {
    private String type;

    public UploadEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
