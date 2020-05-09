package com.qtimes.pavilion.delegate;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liutao on 2017/4/1.
 */

public class BaseDelegateImpl implements BaseDelegate {

    @Override
    public void register() {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void unRegister() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
