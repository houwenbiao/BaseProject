package com.qtimes.wonly.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.events.AppUpgradeEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            PluLog.d("[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                PluLog.d("[JPushReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                PluLog.d("[JPushReceiver] 接收到推送下来的自定义消息的附加JSON字段: " + json);
                JSONObject jsonExtra = new JSONObject(json);
                String update = jsonExtra.getString("update");
                if (update.equals("1")){
                    EventBus.getDefault().post(new AppUpgradeEvent());
                }
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                PluLog.d("[JPushReceiver] 接收到推送下来的通知，ID: " + notifactionId);
                try {
                    String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);//控制帧json字段
                    JSONObject jsonExtra = new JSONObject(extra);
                    String data = jsonExtra.getString("data");
                    JSONObject jsonData = new JSONObject(data);
                    String msg = jsonData.getString("msg");
                    JSONObject jmsg = new JSONObject(msg);
                    if (jmsg.has("type")) {
                        PluLog.i(jmsg);
                    } else {
                        PluLog.i("!!!unknown message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                PluLog.d("[JPushReceiver] 用户点击打开了通知");

                //打开自定义的Activity
//                Intent i = new Intent(context, PushActivity.class);
//                i.putExtras(bundle);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(i);
//                PluLog.i("JPush消息："+);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                PluLog.d("[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                PluLog.w("[JPushReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                PluLog.d("[JPushReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            PluLog.e(e);
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            sb.append("\nkey=" + key + " value=" + bundle.get(key));
        }
        return sb.toString();
    }
}
