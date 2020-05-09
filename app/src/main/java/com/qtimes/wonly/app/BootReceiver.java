package com.qtimes.wonly.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.activity.MainActivity;

/**
 * Author: JackHou
 * Date: 2019/1/17.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            PluLog.i("BOOT_COMPLETED");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
