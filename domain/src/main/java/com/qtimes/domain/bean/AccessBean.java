package com.qtimes.domain.bean;

/**
 * Author: JackHou
 * Date: 2020/2/10.
 */
public class AccessBean {
    private String token;
    private int expireIn;

    public AccessBean(String token, int expireIn) {
        this.token = token;
        this.expireIn = expireIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    @Override
    public String toString() {
        return "AccessBean{" +
                "token='" + token + '\'' +
                ", expireIn=" + expireIn +
                '}';
    }
}
