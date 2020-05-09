package com.qtimes.wonly.events;

/**
 * Author: JackHou
 * Date: 2017/10/26.
 * 打印当前程序进度事件
 */

public class PrintEvent {
    private String msg;

    public PrintEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
