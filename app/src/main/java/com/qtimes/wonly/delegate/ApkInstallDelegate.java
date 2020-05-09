package com.qtimes.wonly.delegate;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;

import com.qtimes.data.constant.Key;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.utils.android.FileUtils;
import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.R;
import com.qtimes.wonly.events.ApkInstallEvent;
import com.qtimes.wonly.utils.ApkInstallUtil;
import com.qtimes.wonly.utils.LogUtil;
import com.qtimes.wonly.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import javax.inject.Inject;

import androidx.core.content.FileProvider;

/**
 * Author: JackHou
 * Date: 2020/5/9.
 */
public class ApkInstallDelegate implements BaseDelegate {

    private Context mContext;
    private DataCache dataCache;

    @Inject
    public ApkInstallDelegate(@ContextLevel(ContextLevel.APPLICATION) Context context,
                              DataCache dataCache) {
        mContext = context;
        this.dataCache = dataCache;
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
    public void onApkInstallEvent(ApkInstallEvent event) {
        installApk(mContext, event.getDownLoadId(), true);
    }


    /**
     * 安装apk
     */
    private void installApk(Context context, Long downloadApkId, Boolean rootMode) {
        // 获取存储ID
        long downId = dataCache.getLongSp(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        long patchDownId = dataCache.getLongSp(Key.Apk.PATCH_DOWNLOAD_ID, -1L);
        //加载基础包
        if (downloadApkId == downId) {
            PluLog.i("install base package");
            ToastUtil.tip(mContext.getString(R.string.install_base_pkg));
            DownloadManager downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                PluLog.i("install base package 1， $downloadFileUri");
                startInstall(context, downloadFileUri, rootMode);
            } else {
                PluLog.e("downloadFileUri is null, install failed");
            }
        }


        /*加载补丁包*/
        if (downloadApkId == patchDownId) {
            PluLog.i("install patch");
            int spv = dataCache.getIntSp(Key.Apk.SERVER_PATCH_VERSIONCODE, -1);
            int sv = dataCache.getIntSp(Key.Apk.SERVER_VERSIONCODE, -1);
            PluLog.i("spv" + spv);
            if (spv != -1) {
                PluLog.i("Tinker install, path:" + context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + context.getResources().getString(R.string.apk_name) + "-" + sv + "-" + spv + ".apk");
//                TinkerInstaller.onReceiveUpgradePatch(context, context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + context.getString(R.string.apk_name) + "-" + sv + "-" + spv + ".apk")
            }
        }
    }

    private void startInstall(Context context, Uri uri, Boolean rootMode) {
        int sv = dataCache.getIntSp(Key.Apk.SERVER_VERSIONCODE, -1);
        if (sv == -1) {
            return;
        }
        String apkPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + context.getResources().getString(R.string.apk_name) + sv + ".apk";
        if (FileUtils.isLwfExist(apkPath)) {
            ApkInstallUtil.setPermission(apkPath);
            if (rootMode) {
                //Root版本直接安装
                boolean success = ApkInstallUtil.installSilent(apkPath);
                if (success) {
                    PluLog.i("Silent Install Success");
                    PowerManager pManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
                    pManager.reboot("重启");
                    ToastUtil.tip(mContext.getString(R.string.install_success));
                } else {
                    LogUtil.i("Silent Install Failed");
                    ToastUtil.tip(mContext.getString(R.string.install_failed_no_permission));
                }
            } else {
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.addCategory(Intent.CATEGORY_DEFAULT);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    File file = new File(apkPath);
                    Uri apkUri = FileProvider.getUriForFile(context, mContext.getString(R.string.package_name), file);//注意需要设置manifests中设置provider以及xml文件
                    installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } else {
                    installIntent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
                }
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(installIntent);
            }
        } else {
            PluLog.i("安装文件不存在");
            ToastUtil.tip(mContext.getString(R.string.install_failed_no_file));
        }
    }
}
