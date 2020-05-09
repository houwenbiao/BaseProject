package com.qtimes.wonly.activity;

import android.content.Context;

import com.google.gson.Gson;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.wonly.base.mvp.BasePresenter;
import com.qtimes.wonly.dagger.PresenterProvide;

import javax.inject.Inject;

/**
 * Author: JackHou
 * Date: 1/3/2018.
 * Presenter 必须在BasePresenter后添加<TestView> 并删除构造函数中的部分代码
 * 构造函数使用@Inject
 */

public class MainPresenter extends BasePresenter<MainView> {

    private Context mContext;
    private Gson gson;
    private DataCache dataCache;

    @Inject
    public MainPresenter(PresenterProvide presenterProvide,
                         Gson gson,
                         DataCache dataCache,
                         @ContextLevel(ContextLevel.ACTIVITY) Context context) {
        super(presenterProvide);
        this.gson = gson;
        this.dataCache = dataCache;
        this.mContext = context;
    }
}
