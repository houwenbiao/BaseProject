package com.qtimes.domain.bean;

import java.io.Serializable;

/**
 * Author: JackHou
 * Date: 2020/5/9.
 */
public class UpdateAppInfo implements Serializable {
    private String app;//app名称
    private String avn;//服务器App版本名称
    private String avc;//服务器上App版本号
    private int ispatch;//是否补丁包
    private int isforce;//强制更新标志
    private String uri;//app最新版本地址
    private String info;//升级信息

    public UpdateAppInfo(String mApp, String mAvn, String mAvc, int mIspatch, int mIsforce, String mUri, String mInfo) {
        app = mApp;
        avn = mAvn;
        avc = mAvc;
        ispatch = mIspatch;
        isforce = mIsforce;
        uri = mUri;
        info = mInfo;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String mApp) {
        app = mApp;
    }

    public String getAvn() {
        return avn;
    }

    public void setAvn(String mAvn) {
        avn = mAvn;
    }

    public String getAvc() {
        return avc;
    }

    public void setAvc(String mAvc) {
        avc = mAvc;
    }

    public int getIspatch() {
        return ispatch;
    }

    public void setIspatch(int mIspatch) {
        ispatch = mIspatch;
    }

    public int getIsforce() {
        return isforce;
    }

    public void setIsforce(int mIsforce) {
        isforce = mIsforce;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String mUri) {
        uri = mUri;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String mInfo) {
        info = mInfo;
    }
}
