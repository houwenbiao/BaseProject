package com.qtimes.pavilion.update;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.qtimes.service.PavilionService;

/**
 * Author: JackHou
 * Date: 2019/10/17.
 */
public class ApkInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 接收安装广播,调试阶段临时注销
        Intent mIntent = new Intent();
        ComponentName componentName = new ComponentName(
                context.getPackageName(),  //被执行启动操作app的包名
                PavilionService.MAIN_ACTIVITY_CLASS_NAME);   //MainActivity路径
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//
        mIntent.setComponent(componentName);
        context.startActivity(mIntent);
    }
}
