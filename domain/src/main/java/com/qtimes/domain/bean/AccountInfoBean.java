package com.qtimes.domain.bean;

import java.io.Serializable;

/**
 * 用户信息实体
 * Created by liutao on 2017/1/3.
 */

public class AccountInfoBean implements Serializable {
    private String username;

    private String token;
    private String expired_in;

    private String os;
    private String uuid;

    private String deviceName;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExpired_in() {
        return expired_in;
    }

    public void setExpired_in(String expired_in) {
        this.expired_in = expired_in;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
