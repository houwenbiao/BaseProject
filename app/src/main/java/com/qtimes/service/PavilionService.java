package com.qtimes.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import com.qtimes.domain.event.PlayerEvent;
import com.qtimes.pavilion.app.App;
import com.qtimes.utils.android.ActivityMgr;
import com.qtimes.utils.android.PluLog;
import com.qtimes.pavilion.R;
import com.qtimes.pavilion.activity.MainActivity;
import com.qtimes.pavilion.app.AppConst;
import com.qtimes.pavilion.app.AppConstant;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * Author: JackHou
 * Date: 2019/1/31.
 */

public class PavilionService extends AbsWorkService {

    //keep alive
    private static final String CHANNEL_ID = "com.qtimes.pavilion.notification.channel";
    public static final String MAIN_ACTIVITY_CLASS_NAME = "com.qtimes.pavilion.activity.MainActivity";
    private static final int NOTIFICATION_ID = 10;
    private static final String NOTIFICATION_TITLE = "智慧收费亭";
    private static final String NOTIFICATION_CONTENT = "请勿关闭！";
    private static final String TAG = "AirService";

    @Override
    public void onCreate() {
        PluLog.i("AirService onCreate");
        super.onCreate();
        //如果保活等级>=3循环播放无声音乐
        if (AppConst.KEEP_ALIVE_LEVEL >= AppConst.KeepAliveLevel.LEVEL3) {
            EventBus.getDefault().post(new PlayerEvent(AppConstant.PlayerType.LULLABY, AppConstant.PlayerAction.LOOP_START, AppConstant.VoiceType.NO_NOTICE));
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PluLog.i("onStartCommand");
        if (AppConst.KEEP_ALIVE_LEVEL >= AppConst.KeepAliveLevel.LEVEL2 && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setForegroundService();
        } else if (AppConst.KEEP_ALIVE_LEVEL >= AppConst.KeepAliveLevel.LEVEL2) {
            setForegroundServiceLow();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected int onStart(Intent intent, int flags, int startId) {
        PluLog.i("AirService________onStart_____________________________");
        return super.onStart(intent, flags, startId);
    }

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return false;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        PluLog.i("AirService________startWork_____________________________");
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        PluLog.i("AirService________stopWork_____________________________");
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        boolean isRunning = ActivityMgr.isActivityRunning(this, PavilionService.MAIN_ACTIVITY_CLASS_NAME);
        if (!isRunning) {
            Intent mIntent = new Intent(getBaseContext(), MainActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(mIntent);
        }
        return isRunning;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }


    /**
     * Notification before API11
     */
    public void setForegroundServiceLow() {
        Notification.Builder builder = new Notification.Builder(App.getInstance().getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.
                getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(App.getInstance().getResources(), R.drawable.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle(NOTIFICATION_TITLE) // 设置下拉列表里的标题
                .setSmallIcon(R.drawable.ic_notification) // 设置状态栏内的小图标
                .setContentText(NOTIFICATION_CONTENT) // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(NOTIFICATION_ID, notification);
    }


    /**
     * 通过通知启动服务
     */
    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    public void setForegroundService() {
        //设定的通知渠道名称
        String channelName = getString(R.string.package_name);
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_LOW;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        channel.setDescription(CHANNEL_ID);
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notification) //设置通知图标(左上角)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))//设置右边图标
                .setContentTitle(NOTIFICATION_TITLE)//设置通知标题
                .setContentText(NOTIFICATION_CONTENT)//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, builder.build());
    }
}
