package com.qtimes.pavilion.delegate;

import com.qtimes.domain.dagger.scope.ApplicationScope;

import javax.inject.Inject;

/**
 * Created by liutao on 2017/1/14.
 */
@ApplicationScope
public class AppDelegateManager extends BaseDelegateManager {
    @Inject
    public AppDelegateManager(DialogDelegate dialogDelegate,
                              PlayerDelegate playerDelegate,
                              ApkInstallDelegate apkInstallDelegate,
                              ApkDownLoadDelegate apkDownLoadDelegate,
                              AppUpgradeDelegate appUpgradeDelegate,
                              UploadDelegate mUploadDelegate,
                              DownloadDelegate downloadDelegate) {
        super(dialogDelegate,
                playerDelegate,
                apkInstallDelegate,
                apkDownLoadDelegate,
                appUpgradeDelegate,
                mUploadDelegate,
                downloadDelegate);
    }
}
