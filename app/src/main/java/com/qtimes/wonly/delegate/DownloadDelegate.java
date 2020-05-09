package com.qtimes.wonly.delegate;

import android.content.Context;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.enums.DownloadCode;
import com.qtimes.wonly.events.DownloadEvent;
import com.qtimes.wonly.events.ProgressEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Author: JackHou
 * Date: 1/4/2018.
 */

public class DownloadDelegate implements BaseDelegate {

    private Context mContext;

    @Inject
    public DownloadDelegate(@ContextLevel(ContextLevel.APPLICATION) Context context) {
        this.mContext = context;
    }

    @Override
    public void register() {
        if (EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadEvent(DownloadEvent event) {
        BaseDownloadTask mBaseDownloadTask = FileDownloader.getImpl().create(event.getUrl())
                .setCallbackProgressMinInterval(5)
                .setPath(event.getPath())
                .setAutoRetryTimes(5)
                .setForceReDownload(true);
        mBaseDownloadTask.setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                int mProgress = 0;
                if (totalBytes != 0) {
                    mProgress = soFarBytes * 100 / totalBytes;
                }
                int timeLeft = 0;
                int speed = mBaseDownloadTask.getSpeed();
                if (speed != 0) {
                    timeLeft = (totalBytes - soFarBytes) / speed / 1000;
                }
                EventBus.getDefault().postSticky(new ProgressEvent(event.getType(), mProgress, timeLeft, speed, DownloadCode.PENDING));
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                PluLog.i("connected:" + totalBytes);
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                int mProgress = 0;
                if (totalBytes != 0) {
                    mProgress = soFarBytes * 100 / totalBytes;
                }
                int timeLeft = 0;
                int speed = mBaseDownloadTask.getSpeed();
                if (speed != 0) {
                    timeLeft = (totalBytes - soFarBytes) / speed / 1000;
                }
                PluLog.i("progress: " + mProgress
                        + "%, speed: " + speed
                        + ", soFarBytes:" + soFarBytes
                        + ", totalBytes: " + totalBytes
                        + ", timeLeft: " + timeLeft);
                EventBus.getDefault().postSticky(new ProgressEvent(event.getType(), mProgress, timeLeft, speed, DownloadCode.PROGRESS));
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
                PluLog.i("blockComplete");
            }

            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                PluLog.i("retry");
                int totalBytes = mBaseDownloadTask.getTotalBytes();
                int mProgress = 0;
                if (totalBytes != 0) {
                    mProgress = soFarBytes * 100 / mBaseDownloadTask.getTotalBytes();
                }
                EventBus.getDefault().postSticky(new ProgressEvent(event.getType(), mProgress, 0, 0, DownloadCode.RETRY));
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                PluLog.i("completed");
                EventBus.getDefault().postSticky(new ProgressEvent(event.getType(), 100, 0, 0, DownloadCode.COMPLETED));
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                PluLog.i("paused");
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                EventBus.getDefault().postSticky(new ProgressEvent(event.getType(), 0, 0, 0, DownloadCode.ERROR));
                PluLog.i("error: " + e);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                PluLog.i("warn");
            }
        }).start();
    }

}
