package com.qtimes.data.respository;

import com.google.gson.Gson;
import com.qtimes.data.cache.MemoryCache;
import com.qtimes.data.net.api.RetrofitHelper;
import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.cache.IMemoryCache;
import com.qtimes.domain.repository.DataRepository;

import javax.inject.Inject;

/**
 * Created by liutao on 2016/5/23.
 */

public class DataRepositoryImpl implements DataRepository {

    protected RetrofitHelper retrofitHelper;
    protected DataCache dataCache;
    protected Gson gson;
    protected AccountCache accountCache;
    @Inject
    public DataRepositoryImpl(RetrofitHelper retrofit, DataCache cache,AccountCache accountCache) {
        this.retrofitHelper = retrofit;
        this.dataCache = cache;
        this.accountCache=accountCache;
        gson = new Gson();
    }

    @Override
    public DataCache getCache() {
        return dataCache;
    }

    @Override
    public IMemoryCache getMemoryCache() {
        return MemoryCache.getInstance();
    }

    @Override
    public AccountCache getAccountCache() {
        return accountCache;
    }
}
