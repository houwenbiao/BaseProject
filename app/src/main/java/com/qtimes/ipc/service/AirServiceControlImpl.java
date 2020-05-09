package com.qtimes.ipc.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.domain.dagger.scope.ApplicationScope;
import com.qtimes.utils.android.AndroidUtil;
import com.qtimes.utils.android.PluLog;

import javax.inject.Inject;

import cn.com.qtimes.ipc.IAirService;

/**
 * 负责airserice发送消息连接
 * Created by liutao on 2017/3/29.
 */
@ApplicationScope
public class AirServiceControlImpl extends DaemonEnv.DaemonServiceCon implements ServiceConnection {
    private IAirService mService;
    private Context mContext;


    @Inject
    public AirServiceControlImpl(@ContextLevel(ContextLevel.APPLICATION) Context context) {
        this.mContext = context;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PluLog.i("AirServiceControlImpl_________________onServiceConnected");
        if (!AndroidUtil.isCurrentProcess(mContext, "com.qtimes.wonly")) {
            PluLog.e("error process");
            return;
        }
        try {
            mService = IAirService.Stub.asInterface(service);
            PluLog.i("AirServiceControlImpl_________________onServiceConnected SUCCESS ");
        } catch (Exception e) {
            PluLog.e("AirServiceControlImpl_________________onServiceConnected : error");
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        PluLog.i("AirServiceControlImpl_________________onServiceDisconnected");
        if (!AndroidUtil.isCurrentProcess(mContext, "com.qtimes.wonly")) {
            PluLog.e("error process");
            return;
        }
        try {
        } catch (Exception e) {
            PluLog.e("AirServiceControlImpl_________________onServiceDisconnected : error");
            e.printStackTrace();
        }
        mService = null;
    }

    public boolean isServiceReady() {
        PluLog.i("AirServiceControlImpl_________________isServiceReady :" + (mService != null));
        return mService != null;
    }


    public void startService() {
        if (!isServiceReady()) {
            PluLog.i("AirServiceControlImpl_________________startService", PluLog.INDEX + 1);
            PluLog.i("startService", PluLog.INDEX + 2);
            PluLog.i("startService", PluLog.INDEX + 3);
            DaemonEnv.startServiceMayBind(AirService.class, this);
        } else {
            PluLog.i("AirServiceControlImpl_________________service already run");
        }
    }

    public void bindService() {
        if (isServiceReady()) {
            PluLog.e("AirServiceControlImpl_________________!!!signaling service is already bound");
            return;
        }
        PluLog.i("AirServiceControlImpl_________________bind signaling service...");
        mContext.bindService(new Intent(mContext, AirService.class), this, Context.BIND_AUTO_CREATE);
    }

    public void unBindService() {
        PluLog.i("AirServiceControlImpl_________________unBindService");
    }
}
