package com.qtimes.pavilion.delegate;

import android.content.Context;
import android.content.Intent;

import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.pavilion.events.DialogEvent;
import com.qtimes.pavilion.tip.QtQuDialogActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * 处理弹框的事件
 * Created by liutao on 2017/2/25.
 */

public class DialogDelegate implements BaseDelegate {
    private AccountCache accountCache;
    private Context context;
    private DataCache dataCache;

    @Inject
    public DialogDelegate(@ContextLevel(ContextLevel.APPLICATION) Context context,
                          AccountCache accountCache,
                          DataCache dataCache) {
        this.accountCache = accountCache;
        this.context = context;
        this.dataCache = dataCache;
    }

    @Override
    public void register() {
        if (EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDialogEvent(DialogEvent event) {
        Intent i = QtQuDialogActivity.getInstance().setDialogType(event.getType()).createIntent(context);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
