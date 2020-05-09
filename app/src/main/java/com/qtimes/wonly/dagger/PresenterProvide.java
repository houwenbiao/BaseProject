package com.qtimes.wonly.dagger;

import android.content.Context;

/**
 * Created by liutao on 2016/12/12.
 */

public class PresenterProvide {
    private Object provider;
    private Context context;

    public PresenterProvide(Context context, Object provider) {
        this.provider = provider;
        this.context = context;
    }

    public Object getProvider() {
        return provider;
    }

    public Context getContext() {
        return context;
    }
}
