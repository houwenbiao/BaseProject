package com.qtimes.wonly.delegate;

import android.content.Context;

import com.qtimes.domain.bean.UpdateAppInfo;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.domain.usecase.QueryUpdateAppUseCase;
import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.R;
import com.qtimes.wonly.utils.AppUpgradeUtil;
import com.qtimes.wonly.utils.ToastUtil;
import com.qtimes.wonly.events.AppUpgradeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * Author: JackHou
 * Date: 2019/12/21.
 */
public class AppUpgradeDelegate implements BaseDelegate, QueryUpdateAppUseCase.QueryUpdateAppCallBack {

    private Context mContext;
    private QueryUpdateAppUseCase updateAppUseCase;
    private DataCache dataCache;

    @Inject
    public AppUpgradeDelegate(@ContextLevel(ContextLevel.APPLICATION) Context context,
                              QueryUpdateAppUseCase updateAppUseCase,
                              DataCache dataCache) {
        mContext = context;
        this.updateAppUseCase = updateAppUseCase;
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


    /**
     * update app
     */
    @Subscribe
    public void queryUpdateAppInfo(AppUpgradeEvent event) {
        updateAppUseCase.execute(new QueryUpdateAppUseCase.QueryUpdateAppReq(mContext.getString(R.string.apk_name), (AppUpgradeUtil.getAppVersionCode(mContext)) + ".00"), this);
    }

    @Override
    public void queryUpdateAppSuccess(UpdateAppInfo info) {
        AppUpgradeUtil.checkUpgrade(info, mContext, dataCache);
    }

    @Override
    public void queryUpdateAppFail(Throwable e) {
        PluLog.e(e);
        ToastUtil.tip(mContext.getString(R.string.check_update_failed));
    }
}
