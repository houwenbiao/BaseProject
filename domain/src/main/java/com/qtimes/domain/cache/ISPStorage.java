package com.qtimes.domain.cache;

/**
 * sp缓存,带sp
 * author: liutao
 * date: 2016/8/11.
 */
public interface ISPStorage {
    void putApplySp(String key, Object value);

    void putSp(String key, Object value);

    long getLongSp(String key, long defaultValue);
    int  getIntSp(String key, int defaultValue);

    String getStringSp(String key, String defaultValue);
    Boolean getBooleanSp(String key, boolean defaultValue);

    boolean removeSP(String keys);
    boolean getBooleanSp(String key);
}
