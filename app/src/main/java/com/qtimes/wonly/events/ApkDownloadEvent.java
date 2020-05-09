package com.qtimes.wonly.events;

/**
 * Author: JackHou
 * Date: 2020/5/9.
 */
public class ApkDownloadEvent {
    private String url;
    private String title;
    private String appName;

    public ApkDownloadEvent(String mUrl, String mTitle, String mAppName) {
        url = mUrl;
        title = mTitle;
        appName = mAppName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String mUrl) {
        url = mUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String mAppName) {
        appName = mAppName;
    }
}
