package com.qtimes.pavilion.events;

import com.qtimes.pavilion.enums.DownloadType;

/**
 * Author: JackHou
 * Date: 2020/2/6.
 */
public class DownloadEvent {
    private String url;
    private String path;
    private DownloadType type;

    public DownloadEvent(String url, String path, DownloadType type) {
        this.url = url;
        this.path = path;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DownloadType getType() {
        return type;
    }

    public void setType(DownloadType type) {
        this.type = type;
    }
}
