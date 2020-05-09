package com.qtimes.domain.bean;

/**
 * Created by hong on 16-12-21.
 */

public class ResultException extends RuntimeException {

    private int errCode = 0;
    private String msg;

    public ResultException(int code, String msg) {
        super(msg);
        this.msg = msg;
        errCode = code;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String mMsg) {
        msg = mMsg;
    }
}
