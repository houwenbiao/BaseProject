package com.qtimes.pavilion.events;

/**
 * Author: JackHou
 * Date: 1/11/2018.
 */

public class FinishEvent {
    int dialogType;
    int type;

    public FinishEvent(int dialogType, int type) {
        this.dialogType = dialogType;
        this.type = type;
    }

    public int getDialogType() {
        return dialogType;
    }

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
