package com.qtimes.domain.repository;

import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.cache.IMemoryCache;

/**
 * 基类
 * =======
 */
public interface DataRepository {

    DataCache getCache();

    IMemoryCache getMemoryCache();

    AccountCache getAccountCache();
}
