package com.qtimes.wonly.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.qtimes.utils.android.PluLog;
import com.qtimes.utils.android.PropertyUtils;

/**
 * 拨号键跳转广播
 */

public class AndroidDialerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        //Intent { act=android.provider.Telephony.SECRET_CODE dat=android_secret_code://1234 flg=0x10 cmp=com.wtt/.receiver.MyBroadcastReceiver }
        // uri --> android_secret_code://1234
        //  host  -->  1234
        Uri uri = intent.getData();
        assert uri != null;
        String host = uri.getHost();
        PluLog.i("host:" + host);
        assert host != null;
        switch (host) {

            case AppConst.DialerHost.OPEN_ADB:
                PropertyUtils.openAdb();
                break;

            case AppConst.DialerHost.CLOSE_ADB:
                PropertyUtils.closeAdb();
                break;
            default:
                break;
        }
    }
}
