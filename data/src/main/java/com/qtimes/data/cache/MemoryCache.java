package com.qtimes.data.cache;
import androidx.collection.LruCache;
import com.qtimes.domain.cache.IMemoryCache;

/**
 * Created by jishubu1 on 2016/6/30.
 * 内存数据1m缓存
 */

public class MemoryCache implements IMemoryCache {

    private static MemoryCache mMemoryCache;
    private static LruCache<String, Object> mLruCache;

    private MemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);//32M
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 32;//1M数据空间
        mLruCache = new LruCache<>(cacheSize);
    }

    public static MemoryCache getInstance() {
        synchronized (MemoryCache.class) {
            if (mMemoryCache == null) {
                mMemoryCache = new MemoryCache();
            }
            return mMemoryCache;
        }
    }

    @Override
    public boolean put(String key, Object value) {
        try {
            mLruCache.put(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object get(String key) {
        try {
            return mLruCache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeApplyList(String[] keys) {
        for (String key : keys) {
            mLruCache.remove(key);
        }
    }
}
