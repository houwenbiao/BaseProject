package com.qtimes.domain.bean;

/**
 * Author: JackHou
 * Date: 2020/2/10.
 */
public class AuthenticateBean {
    private String productKey;
    private String deviceName;
    private String deviceSecret;

    public AuthenticateBean(String productKey, String deviceName, String deviceSecret) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.deviceSecret = deviceSecret;
    }

    @Override
    public String toString() {
        return "ActivateBean{" +
                "productKey='" + productKey + '\'' +
                ", passcode='" + deviceSecret + '\'' +
                ", deviceName='" + deviceName + '\'' +
                '}';
    }
}
