package com.qtimes.domain.cache;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by PLU on 2016/6/3.
 */
public interface DataCache extends ISPStorage  {
    <T> T getAsGson(String key, Class<T> classOfT);

    <T> T getAsGson(String key, Type type);

    void putAsGson(String key, Object value);

    Object getAsObject(String key, Object def);
    Object getAsObject(String key);
    String getAsString(String key);

    String getAsString(String key, String def);

    Bitmap getAsBitmap(String key);
    void putString(String key, String value);
    void put(String key, Serializable value);
    void put(String key, Serializable value, int time);
    void put(String key, JSONArray value, int time);
    void put(String key, Bitmap bitmap);
    void putObj(String key, Object value);

    void putAsGson(String key, Object value, int time);

    void put(String key, String value, int saveTime);

    void clearData(String key);

    boolean getBoolean(Context ct, String key, boolean defValue);

    void clear();

}
