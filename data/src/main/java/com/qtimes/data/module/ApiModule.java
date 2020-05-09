package com.qtimes.data.module;

import android.content.Context;

import com.google.gson.Gson;
import com.qtimes.data.BuildConfig;
import com.qtimes.data.cache.ACache;
import com.qtimes.data.cache.AccountCacheImpl;
import com.qtimes.data.cache.DataCacheImpl;
import com.qtimes.data.cache.MemoryCache;
import com.qtimes.data.cache.SPStorageUtil;
import com.qtimes.data.net.Api;
import com.qtimes.data.net.interceptor.DefaultHeaderInterceptor;
import com.qtimes.data.net.interceptor.HttpErrorInterceptor;
import com.qtimes.data.net.service.DeviceService;
import com.qtimes.data.net.service.ApiService;
import com.qtimes.data.respository.ApiRepositoryImpl;
import com.qtimes.data.respository.DeviceRepositoryImpl;
import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.cache.IMemoryCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.domain.dagger.scope.ApplicationScope;
import com.qtimes.domain.repository.DeviceDataRepository;
import com.qtimes.domain.repository.ApiDataRepository;
import com.qtimes.utils.android.PluLog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by liutao on 2016/12/12.
 */
@Module
public class ApiModule {
    @Provides
    @ApplicationScope
    Map<Class<?>, String> provideApiMap() {
        Map<Class<?>, String> map = new HashMap<>();
        map.put(ApiService.class, Api.URL.API);
        map.put(DeviceService.class, Api.URL.DEVICE);
        return map;
    }

    @Provides
    @ApplicationScope
    public Set<Interceptor> provideDefInterceptor(DefaultHeaderInterceptor defaultHeaderInterceptor, HttpErrorInterceptor mHttpErrorInterceptor) {
        Set<Interceptor> interceptorSet = new HashSet<>();
        interceptorSet.add(defaultHeaderInterceptor);
        interceptorSet.add(mHttpErrorInterceptor);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    PluLog.d("okhttp" + message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            interceptorSet.add(loggingInterceptor);
        }

        return interceptorSet;
    }

    @Provides
    @ApplicationScope
    public ACache provideACache(@ContextLevel(ContextLevel.APPLICATION) Context context) {
        return ACache.get(context);
    }

    @Provides
    @ApplicationScope
    public AccountCache provideAccountCache(AccountCacheImpl cache, @ContextLevel(ContextLevel.APPLICATION) Context c) {
        return cache;
    }

    @Provides
    @ApplicationScope
    public SPStorageUtil provideSPStorageUtil(@ContextLevel(ContextLevel.APPLICATION) Context context) {
        return SPStorageUtil.get(context);
    }

    @Provides
    @ApplicationScope
    public DataCache provideDataCache(DataCacheImpl dataCache) {
        return dataCache;
    }

    @Provides
    @ApplicationScope
    public IMemoryCache provideMemoryCache(MemoryCache dataCache) {
        return dataCache;
    }

    @Provides
    @ApplicationScope
    public ApiDataRepository provideTestDataRepository(ApiRepositoryImpl repository) {
        return repository;
    }

    @Provides
    @ApplicationScope
    public DeviceDataRepository provideDeviceDataRepository(DeviceRepositoryImpl repository) {
        return repository;
    }

    @Provides
    @ApplicationScope
    public Gson provideGson() {
        return new Gson();
    }
}
