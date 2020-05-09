package com.qtimes.wonly.events;

/**
 * Author: JackHou
 * Date: 2020/4/2.
 * 功能开关
 */
public class FunctionOpenEvent {
    private int funcId;
    private boolean open;
    private String desc;

    public FunctionOpenEvent(int mFuncId, boolean mOpen) {
        funcId = mFuncId;
        open = mOpen;
    }

    public int getFuncId() {
        return funcId;
    }

    public void setFuncId(int mFuncId) {
        funcId = mFuncId;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean mOpen) {
        open = mOpen;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String mDesc) {
        desc = mDesc;
    }

    @Override
    public String toString() {
        return "FunctionOpenEvent{" +
                "funcId=" + funcId +
                ", open=" + open +
                ", desc='" + desc + '\'' +
                '}';
    }
}
