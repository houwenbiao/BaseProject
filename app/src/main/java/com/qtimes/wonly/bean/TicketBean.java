package com.qtimes.wonly.bean;

import java.io.Serializable;

/**
 * Author: JackHou
 * Date: 2020/3/16.
 * 工单实体
 */
public class TicketBean implements Serializable {
    private int ticketId;
    private int ticketType;
    private int status;
    private String userName;
    private String userPhone;
    private String supportName;
    private String supportPhone;
    private String info;
    private String deviceName;

    public TicketBean() {
    }

    public TicketBean(int ticketId, int ticketType, int status) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.status = status;
    }

    public TicketBean(String userName, String userPhone, String supportName, String supportPhone) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.supportName = supportName;
        this.supportPhone = supportPhone;
    }

    public TicketBean(int ticketId, int ticketType, int status, String userName, String userPhone, String supportName, String supportPhone) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.status = status;
        this.userName = userName;
        this.userPhone = userPhone;
        this.supportName = supportName;
        this.supportPhone = supportPhone;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }

    public String getSupportPhone() {
        return supportPhone;
    }

    public void setSupportPhone(String supportPhone) {
        this.supportPhone = supportPhone;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "TicketBean{" +
                "ticketId=" + ticketId +
                ", ticketType=" + ticketType +
                ", status=" + status +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", supportName='" + supportName + '\'' +
                ", supportPhone='" + supportPhone + '\'' +
                ", info='" + info + '\'' +
                ", deviceName='" + deviceName + '\'' +
                '}';
    }
}
