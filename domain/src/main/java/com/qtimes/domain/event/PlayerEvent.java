package com.qtimes.domain.event;

/**
 * Author: JackHou
 * Date: 1/4/2018.
 */

public class PlayerEvent {
    private int type;//音乐类型,后续增加
    private int action;//暂停、播放、停止
    private int source;

    public PlayerEvent(int action) {
        this.action = action;
    }

    public PlayerEvent(int type, int action) {
        this.type = type;
        this.action = action;
    }

    public PlayerEvent(int type, int action, int source) {
        this.type = type;
        this.action = action;
        this.source = source;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
