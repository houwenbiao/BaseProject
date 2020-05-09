package com.qtimes.wonly.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.utils.AlarmManagerUtil;

/**
 * 闹钟广播
 * Created by liutao on 2017/2/7.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PluLog.e("receiver alarm=========");
        int id = intent.getIntExtra("type", 0);
        String time = intent.getStringExtra("time");
        long intervalMillis = intent.getLongExtra("intervalMillis", 0);
        if (intervalMillis != 0) {
            PluLog.e("定时闹钟alarm, intervalMillis: " + intervalMillis);
            AlarmManagerUtil.setAlarmTime(context, System.currentTimeMillis(), intervalMillis, intent);
        }
//        EventBus.getDefault().post(new QueryContactEvent());
    }
}
