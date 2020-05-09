package com.qtimes.data.cache;

import android.content.Context;

import com.qtimes.data.constant.Key;
import com.qtimes.domain.bean.AccountInfoBean;
import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.domain.dagger.scope.ApplicationScope;

import javax.inject.Inject;

/**
 * Created by liutao on 2017/1/3.
 */
@ApplicationScope
public class AccountCacheImpl implements AccountCache {
    private DataCache dataCache;
    private AccountInfoBean userInfo;
    private Context context;

    @Inject
    public AccountCacheImpl(@ContextLevel(ContextLevel.APPLICATION) Context context, DataCache dataCache) {
        this.context = context;
        this.dataCache = dataCache;
        userInfo = getUserInfo();
    }

    @Override
    public synchronized AccountInfoBean getUserInfo() {
        if (userInfo != null) {
            return userInfo;
        }
        synchronized (this) {
            if (userInfo == null) {
                userInfo = new AccountInfoBean();
                userInfo.setToken(dataCache.getStringSp(Key.Account.ACCESS_TOKEN, ""));
                userInfo.setDeviceName(dataCache.getStringSp(Key.Account.DEVICE_NAME, ""));
            }
        }
        return userInfo;
    }
}
