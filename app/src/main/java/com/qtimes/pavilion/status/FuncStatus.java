package com.qtimes.pavilion.status;

/**
 * Author: JackHou
 * Date: 2020/4/21.
 */
public enum FuncStatus {
    RESOLVED(0, "保留"),
    OPEN(1, "开启"),
    CLOSE(2, "关闭");

    int code;
    String desc;

    FuncStatus(int mCode, String mDesc) {
        code = mCode;
        desc = mDesc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int mCode) {
        code = mCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String mDesc) {
        desc = mDesc;
    }

    @Override
    public String toString() {
        return "FuncStatus{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
