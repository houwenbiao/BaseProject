package com.qtimes.data.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.qtimes.domain.cache.DataCache;

import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * 缓存实现类
 * Created by PLU on 2016/6/3.
 */
public class DataCacheImpl implements DataCache {
    private ACache aCache;
    private Gson gson;
    private SPStorageUtil spStorageUtil;

    @Inject
    public DataCacheImpl(ACache aCache, SPStorageUtil spStorageUtil) {
        this.aCache = aCache;
        this.spStorageUtil = spStorageUtil;
        gson = new Gson();
    }

    @Override
    public <T> T getAsGson(String key, Class<T> classOfT) {
        return aCache.getAsGson(key, classOfT);
    }

    @Override
    public <T> T getAsGson(String key, Type type) {
        return aCache.getAsGson(key, type);
    }

    @Override
    public void putAsGson(String key, Object value) {
        aCache.putAsGson(key, value);
    }

    @Override
    public Object getAsObject(String key, Object def) {
        return aCache.getAsObject(key, def);
    }

    @Override
    public Object getAsObject(String key) {
        return aCache.getAsObject(key);
    }


    @Override
    public String getAsString(String key) {
        return aCache.getAsString(key);
    }

    @Override
    public String getAsString(String key, String def) {
        return spStorageUtil.getAsString(key, def);
    }

    @Override
    public Bitmap getAsBitmap(String key) {
        return aCache.getAsBitmap(key);
    }

    public void putString(String key, String value) {
        spStorageUtil.put(key, value);
    }

    @Override
    public void put(String key, Serializable value) {
        aCache.put(key, value);
    }

    @Override
    public void put(String key, Serializable value, int time) {
        aCache.put(key, value, time);
    }

    @Override
    public void put(String key, JSONArray value, int time) {
        aCache.put(key, value, time);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        aCache.put(key, bitmap);
    }

    @Override
    public void putObj(String key, Object value) {
        if (value instanceof String) {
            aCache.put(key, (String) value);
        } else if (value instanceof Bitmap) {
            aCache.put(key, (Bitmap) value);
        }
    }


    @Override
    public void putAsGson(String key, Object value, int time) {
        aCache.putAsGson(key, value, time);
    }

    @Override
    public void put(String key, String value, int saveTime) {
        aCache.put(key, value, saveTime);
    }


    @Override
    public void clearData(String key) {
        aCache.remove(key);
    }

    @Override
    public boolean getBoolean(Context ct, String key, boolean defValue) {
        return SPStorageUtil.getBoolean(ct, key, defValue);
    }

    @Override
    public void clear() {
        aCache.clear();
    }


    @Override
    public void putApplySp(String key, Object value) {
        spStorageUtil.putApply(key, value);
    }

    @Override
    public void putSp(String key, Object value) {
        spStorageUtil.put(key, value);
    }

    @Override
    public long getLongSp(String key, long defaultValue) {
        return spStorageUtil.getLong(key, defaultValue);
    }

    @Override
    public int getIntSp(String key, int defaultValue) {
        return spStorageUtil.getInt(key, defaultValue);
    }

    @Override
    public String getStringSp(String key, String defaultValue) {
        return spStorageUtil.getAsString(key, defaultValue);
    }

    @Override
    public Boolean getBooleanSp(String key, boolean defaultValue) {
        return spStorageUtil.getAsBoolean(key, defaultValue);
    }

    @Override
    public boolean removeSP(String keys) {
        return spStorageUtil.remove(keys);
    }

    @Override
    public boolean getBooleanSp(String key) {
        return spStorageUtil.getAsBoolean(key);
    }


}
