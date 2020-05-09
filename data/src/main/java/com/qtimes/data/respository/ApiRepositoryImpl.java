package com.qtimes.data.respository;

import com.google.gson.Gson;
import com.qtimes.data.bean.BaseResponse;
import com.qtimes.data.net.api.RetrofitHelper;
import com.qtimes.data.net.service.ApiService;
import com.qtimes.data.util.BaseMapper;
import com.qtimes.domain.bean.UpdateAppInfo;
import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.repository.ApiDataRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Author: JackHou
 * Date: 2019/3/6.
 */
public class ApiRepositoryImpl extends DataRepositoryImpl implements ApiDataRepository {

    protected RetrofitHelper helper;
    protected AccountCache accountCache;
    protected Gson gson;

    @Inject
    public ApiRepositoryImpl(RetrofitHelper retrofit, DataCache cache, AccountCache accountCache) {
        super(retrofit, cache, accountCache);
        this.helper = retrofit;
        this.accountCache = accountCache;
        gson = new Gson();
    }

    private ApiService createWonlyService() {
        return helper.createService(ApiService.class);
    }


    @Override
    public Observable<UpdateAppInfo> queryUpdateAppInfo(String app, String avc) {
        return createWonlyService()
                .queryUpdateAppInfo(app, avc)
                .map(new BaseMapper<BaseResponse<UpdateAppInfo>, UpdateAppInfo>());
    }
}
