package com.qtimes.pavilion.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qtimes.utils.android.PluLog;
import com.qtimes.pavilion.R;
import com.qtimes.pavilion.events.ApkInstallEvent;
import com.qtimes.pavilion.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Author: JackHou
 * Date: 2018/2/27.
 */

public class ApkInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PluLog.i("ApkInstallReceiver, action:" + intent.getAction());
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE) || intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            FileDownloadManager fdm = FileDownloadManager.getInstance(context);
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            int status = fdm.getDownloadStatus(downloadApkId);
            PluLog.i("ApkInstallReceiver downloadApkId & status " + downloadApkId + "   " + status);
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    PluLog.i("下载暂停");
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    PluLog.i("下载延迟");
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    PluLog.i("正在下载");
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    EventBus.getDefault().post(new ApkInstallEvent(downloadApkId));
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    ToastUtil.tip(context.getString(R.string.apk_download_failed));
                    break;
            }
        }
    }
}
