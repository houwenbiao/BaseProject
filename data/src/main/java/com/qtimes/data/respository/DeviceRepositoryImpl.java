package com.qtimes.data.respository;

import com.google.gson.Gson;
import com.qtimes.data.bean.BaseResponse;
import com.qtimes.data.net.api.RetrofitHelper;
import com.qtimes.data.net.service.DeviceService;
import com.qtimes.data.util.BaseMapper;
import com.qtimes.domain.bean.AuthenticateBean;
import com.qtimes.domain.bean.AuthenticateRes;
import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.repository.DeviceDataRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Author: JackHou
 * Date: 2019/3/6.
 */
public class DeviceRepositoryImpl extends DataRepositoryImpl implements DeviceDataRepository {

    protected RetrofitHelper helper;
    protected AccountCache accountCache;
    protected Gson gson;

    @Inject
    public DeviceRepositoryImpl(RetrofitHelper retrofit, DataCache cache, AccountCache accountCache) {
        super(retrofit, cache, accountCache);
        this.helper = retrofit;
        this.accountCache = accountCache;
        gson = new Gson();
    }

    private DeviceService createDeviceService() {
        return helper.createService(DeviceService.class);
    }

    @Override
    public Observable<AuthenticateRes> authenticate(AuthenticateBean mAuthenticateBean) {
        return createDeviceService()
                .authenticate(mAuthenticateBean).map(new BaseMapper<BaseResponse<AuthenticateRes>, AuthenticateRes>());
    }
}
