package com.qtimes.wonly.events;

import com.qtimes.wonly.status.NetBean;
import com.qtimes.utils.rx.RxNetUtil;

/**
 * Created by hong on 17-1-8.
 */

public class StatusEvent {
    public final static int NET=0X001;
    public final static int HEARTBEAT=0X002;

    private int type;
    private int action;
    private RxNetUtil.NetStatus status;
    public StatusEvent(int type, int action){
        this.type=type;
        this.action=action;
    }
    public StatusEvent(RxNetUtil.NetStatus netStatus){
        this.status=netStatus;
        this.type=NET;
        this.action= NetBean.DEFAULT;
    }
    public int getType() {
        return type;
    }

    public int getAction() {
        return action;
    }

    public RxNetUtil.NetStatus getStatus() {
        return status;
    }
}
