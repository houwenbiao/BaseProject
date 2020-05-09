package com.qtimes.data.net.api;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gufei on 2016/8/8.
 */
public class HttpCacheMap {
    static HttpCacheMap mHttpCacheMap;
    private LinkedHashMap<String, HttpCache> linkedHashMap;

    public HttpCacheMap() {
        linkedHashMap = new LinkedHashMap<>();
    }

    public static HttpCacheMap getInstance() {
        synchronized (HttpCacheMap.class) {
            if (mHttpCacheMap == null) {
                mHttpCacheMap = new HttpCacheMap();
            }
            return mHttpCacheMap;
        }
    }

    public void put(Response response) {
        if (response == null || response.isSuccessful() == false) return;
        String expires = response.header("Expires");
        if (TextUtils.isEmpty(expires) || "-1".equals(expires)) return;//no key or -1
        String cacheControl = response.header("Cache-Control");
        if (TextUtils.isEmpty(cacheControl)) return;

        HttpCache httpCache = new HttpCache();
        httpCache.setCacheControl(cacheControl);
        httpCache.setExpires(expires);
        String url = response.request().url().toString();
        linkedHashMap.put(url, httpCache);
    }

    public HttpCache get(Request request) {
        String url = request.url().url().toString();
        if (linkedHashMap == null || linkedHashMap.containsKey(url) == false) return null;
        HttpCache httpCache = linkedHashMap.get(url);
        CacheControl cacheControl = request.cacheControl();
        if (cacheControl != null) {
            String cacheStr = cacheControl.toString();
            httpCache.setCacheControl(cacheStr);
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            Date date = sdf.parse(httpCache.getExpires());
            long nowTime = System.currentTimeMillis();
//            PluLog.d("HttpCacheMap:nowTime=" + nowTime + "|cacheTime=" + date.getTime());
//            if (date.getTime() < nowTime) {//缓存时间小于本地时间，失效
//                linkedHashMap.remove(url);
//                httpCache = null;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            linkedHashMap.remove(url);
            httpCache = null;
        }
//        PluLog.d("HttpCacheMap:url=" + url + "|" + httpCache);

        return httpCache;
    }

    public class HttpCache {
        String cacheControl;//Cache-Control: max-age=2727
        String expires;//        Expires: Mon, 08 Aug 2016 03:36:53 GMT

        public String getCacheControl() {
            return cacheControl;
        }

        public void setCacheControl(String cacheControl) {
            this.cacheControl = cacheControl;
        }

        public String getExpires() {
            return expires;
        }

        public void setExpires(String expires) {
            this.expires = expires;
        }
    }
}
