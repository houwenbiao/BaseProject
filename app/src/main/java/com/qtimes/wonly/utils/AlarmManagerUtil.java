package com.qtimes.wonly.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import com.qtimes.wonly.app.AlarmReceiver;
import com.qtimes.wonly.app.AppConst;
import com.qtimes.wonly.bean.AlarmBean;
import com.qtimes.utils.android.PluLog;

import java.util.Calendar;

/**
 * Created by loonggg on 2016/3/21.
 */
public class AlarmManagerUtil {
    public static final String ALARM_ACTION = "com.qtimes.wonly.clock";


    public static void setAlarmTime(Context context, long timeInMillis, long intervalMillis, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra("type", 0),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis + intervalMillis, sender);
            PluLog.i("alarm->" + "setExactAndAllowWhileIdle：" + (timeInMillis + intervalMillis));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, timeInMillis + intervalMillis, sender);//精确闹钟
            PluLog.i("alarm->" + "setExact:" + (timeInMillis + intervalMillis));
        }
    }

    //取消一个闹钟
    public static void cancelAlarm(Context context, String action, int type) {
        Intent intent = new Intent(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, type, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        PluLog.i("cancelAlarm:" + type);
    }


    public static void setAlarm(Context context, AlarmBean alarmBean) {
        PluLog.i("setAlarmBean:" + alarmBean.getTime());
        if (!alarmBean.hasData || !alarmBean.isAlarm()) {
            cancelAlarm(context, ALARM_ACTION, alarmBean.getType());
            return;
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long intervalMillis = 0;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), alarmBean.getAllHourNoMinute(), alarmBean.getMinute(), alarmBean.getSecond());

        PluLog.i("alarm0, " + alarmBean.getAllHourNoMinute() + ":" + alarmBean.getMinute() + ":" + alarmBean.getSecond());

        long selectTime = calendar.getTimeInMillis();
        long current = System.currentTimeMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (current > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (alarmBean.getFlag() == AppConst.AlarmFlag.SINGLE) {
            intervalMillis = 0;
        } else if (alarmBean.getFlag() == AppConst.AlarmFlag.DAY) {
            intervalMillis = 24 * 3600 * 1000;
        } else if (alarmBean.getFlag() == AppConst.AlarmFlag.WEEK) {
            intervalMillis = 24 * 3600 * 1000 * 7;
        } else if (alarmBean.getFlag() == AppConst.AlarmFlag.HOUR) {
            intervalMillis = 3600 * 1000;
        } else if (alarmBean.getFlag() == AppConst.AlarmFlag.MINUTE) {
            intervalMillis = 60 * 1000;
        } else if (alarmBean.getFlag() == AppConst.AlarmFlag.CUSTOM) {//自定义循环时间单位秒
            intervalMillis = 1000 * alarmBean.getInterval();
        }

        LogUtil.i("intervalMillis: " + intervalMillis);

        //android8.0静态注册的广播接收者无法接受 隐式 广播
//        Intent intent = new Intent(ALARM_ACTION);//隐士intent
//        intent.setComponent(new ComponentName("cn.com.qtimes.libs",
//                "cn.com.qtimes.libs.app.AlarmReceiver"));
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ALARM_ACTION);
        intent.putExtra("type", alarmBean.getType());
        intent.putExtra("intervalMillis", intervalMillis);
        intent.putExtra("time", alarmBean.getTime());

        PendingIntent sender = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            PluLog.i("alarm->" + "setExactAndAllowWhileIdle:" + calendar.getTimeInMillis());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);//精确闹钟
            PluLog.i("alarm->" + "setExact:" + calendar.getTimeInMillis());
        } else {
            if (alarmBean.getFlag() == 0) {
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                PluLog.i("alarm->" + "set:" + calendar.getTimeInMillis());
            } else {
                am.setRepeating(AlarmManager.RTC_WAKEUP, calMethod(alarmBean.getWeek(), calendar.getTimeInMillis()), intervalMillis, sender);
                PluLog.i("alarm->" + "setRepeating:" + intervalMillis);
            }
        }
    }


    /**
     * @param weekflag 传入的是周几
     * @param dateTime 传入的是时间戳（设置当天的年月日+从选择框拿来的时分秒）
     * @return 返回起始闹钟时间的时间戳
     */
    private static long calMethod(int weekflag, long dateTime) {
        long time = 0;
        //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
        if (weekflag != 0) {
            Calendar c = Calendar.getInstance();
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (1 == week) {
                week = 7;
            } else if (2 == week) {
                week = 1;
            } else if (3 == week) {
                week = 2;
            } else if (4 == week) {
                week = 3;
            } else if (5 == week) {
                week = 4;
            } else if (6 == week) {
                week = 5;
            } else if (7 == week) {
                week = 6;
            }

            if (weekflag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime;
                } else {
                    time = dateTime + 7 * 24 * 3600 * 1000;
                }
            } else if (weekflag > week) {
                time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
            } else if (weekflag < week) {
                time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
            }
        } else {
            if (dateTime > System.currentTimeMillis()) {
                time = dateTime;
            } else {
                time = dateTime + 24 * 3600 * 1000;
            }
        }
        return time;
    }
}
