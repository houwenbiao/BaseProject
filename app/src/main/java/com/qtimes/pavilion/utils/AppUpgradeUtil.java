package com.qtimes.pavilion.utils;

import android.content.Context;

import com.qtimes.data.constant.Key;
import com.qtimes.domain.bean.UpdateAppInfo;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.utils.android.AndroidUtil;
import com.qtimes.utils.android.PluLog;
import com.qtimes.pavilion.R;
import com.qtimes.pavilion.update.FileDownloadManager;
import com.qtimes.pavilion.events.ApkDownloadEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Author: JackHou
 * Date: 2018/3/5.
 */

public class AppUpgradeUtil {

    /**
     * 获取已安装App的versionCode
     * 因为增加了patch版本所以需要添加这步检测
     *
     * @param context
     * @return
     */
    public static String getAppVersionCode(Context context) {
        String localPatchVersionCode = "-1"/*Utils.getPatchVersion(context)*/;//localPatchVersionCode, Tinker暂时不加
        int localVersionCode = AndroidUtil.getAppVersion(context);//localVersionCode
        String appVersionCode = String.valueOf(localVersionCode);
        PluLog.i("localPatchVersionCode:" + localPatchVersionCode + "localVersionCode:" + appVersionCode);
        if (!localPatchVersionCode.equals("-1")) {
            appVersionCode = localPatchVersionCode;
        }
        PluLog.i("appVersionCode:" + appVersionCode);
        return appVersionCode;
    }


    /**
     * 检测更新
     *
     * @param info
     * @param mContext
     * @param dataCache
     */
    public static void checkUpgrade(UpdateAppInfo info, Context mContext, DataCache dataCache) {
        boolean lastForce = info.getIsforce() == 1;
        boolean isPatch = info.getIspatch() == 1;
        String updateInfo = info.getInfo();
        String appName = info.getApp();
        double serverVersionCode = Double.parseDouble(info.getAvc());
        double appVersionCode = Double.parseDouble(getAppVersionCode(mContext));
        PluLog.i("serverVersionCode:" + serverVersionCode + ", appVersionCode:" + appVersionCode);
        String localAppName = AndroidUtil.getAppName(mContext);
//        //测试小趣端版本升级，直接写在这里，正式的需要商讨字段定义，后面的暂时注释掉
//        EventBus.getDefault().post(new DialogEvent(DialogType.DIALOG_UPDATE_QU, info));
        String avc = info.getAvc().replace(".00", "");
        if ((appName.equals(localAppName) || appName.equals(mContext.getString(R.string.apk_name)))) {
            if (serverVersionCode > appVersionCode) {
                if (isPatch) {
                    long patchDownLoadId = FileDownloadManager.getInstance(mContext).startDownload(info.getUri(),
                            mContext.getString(R.string.patch_downloading),
                            mContext.getString(R.string.restart_app_patch_download),
                            mContext.getResources().getString(R.string.apk_name) + avc);
                    PluLog.i("patchDownLoadId:" + patchDownLoadId + "app:" + mContext.getResources().getString(R.string.apk_name) + avc + ".apk");
                    dataCache.putSp(Key.Apk.SERVER_VERSIONCODE, Integer.parseInt(avc));
                    dataCache.putSp(Key.Apk.PATCH_DOWNLOAD_ID, patchDownLoadId);
                } else {
                    EventBus.getDefault().post(new ApkDownloadEvent(info.getUri(),
                            mContext.getString(R.string.apk_downloading),
                            mContext.getResources().getString(R.string.apk_name) + avc));
                    dataCache.putSp(Key.Apk.SERVER_VERSIONCODE, Integer.parseInt(avc));
                }
            } else {
                ToastUtil.tip(mContext.getString(R.string.lasted_version));
            }
        }
    }
}
