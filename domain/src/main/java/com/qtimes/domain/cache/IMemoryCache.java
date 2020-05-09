package com.qtimes.domain.cache;

/**
 * Created by jishubu1 on 2016/6/30.
 */
public interface IMemoryCache {

    boolean put(String key, Object value);

    Object get(String key);

    void removeApplyList(String[] keys);
}
