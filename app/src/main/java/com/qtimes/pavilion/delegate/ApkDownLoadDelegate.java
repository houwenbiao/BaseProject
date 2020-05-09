package com.qtimes.pavilion.delegate;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.qtimes.data.constant.Key;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.utils.android.AndroidUtil;
import com.qtimes.utils.android.PluLog;
import com.qtimes.pavilion.R;

import com.qtimes.pavilion.events.ApkDownloadEvent;
import com.qtimes.pavilion.events.ApkInstallEvent;
import com.qtimes.pavilion.update.FileDownloadManager;
import com.qtimes.pavilion.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Author: JackHou
 * Date: 2018/2/27.
 */

public class ApkDownLoadDelegate extends BaseDelegateImpl {
    private Context mContext;
    private DataCache dataCache;

    @Inject
    public ApkDownLoadDelegate(@ContextLevel(ContextLevel.APPLICATION) Context mContext, DataCache dataCache) {
        this.dataCache = dataCache;
        this.mContext = mContext;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void download(ApkDownloadEvent event) {
        // 获取存储ID
        long downloadId = dataCache.getLongSp(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        PluLog.i("downloadId:" + downloadId);
        if (downloadId != -1L) {
            FileDownloadManager fdm = FileDownloadManager.getInstance(mContext);
            int status = fdm.getDownloadStatus(downloadId);
            PluLog.i("status: " + status);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                //启动更新界面
                Uri uri = fdm.getDownloadUri(downloadId);
                long sv = dataCache.getIntSp(Key.Apk.SERVER_VERSIONCODE, -1);
                String path = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + mContext.getResources().getString(R.string.apk_name) + String.valueOf(sv).replace(".00", "") + ".apk";
                PluLog.i(path);
                if (uri != null && sv != -1) {
                    if (AndroidUtil.compare(AndroidUtil.getApkInfo(mContext, path), mContext)) {
                        EventBus.getDefault().post(new ApkInstallEvent(downloadId));
                        return;
                    } else {
                        fdm.getDownloadManager().remove(downloadId);
                    }
                }
                start(mContext, event.getUrl(), event.getTitle(), event.getAppName());
            } else if (status == DownloadManager.STATUS_RUNNING) {
                ToastUtil.tip(mContext.getString(R.string.apk_downloading));
            } else if (status == DownloadManager.STATUS_FAILED) {
                PluLog.i("Download Failed");
                start(mContext, event.getUrl(), event.getTitle(), event.getAppName());
            } else if (status == DownloadManager.STATUS_PAUSED) {
                PluLog.i("Download Paused");
                start(mContext, event.getUrl(), event.getTitle(), event.getAppName());
            } else {
                start(mContext, event.getUrl(), event.getTitle(), event.getAppName());
            }
        } else {
            start(mContext, event.getUrl(), event.getTitle(), event.getAppName());
        }
    }

    private void start(Context context, String url, String title, String appName) {
        long id = FileDownloadManager.getInstance(context).startDownload(url, title,
                mContext.getString(R.string.auto_install_after_download), appName);
        dataCache.putSp(DownloadManager.EXTRA_DOWNLOAD_ID, id);
        PluLog.d("apk start download, id = " + id);
        PluLog.i("开始下载新版本");
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                ToastUtil.tip(mContext.getString(R.string.start_download_apk));
            }
        });
    }
}
