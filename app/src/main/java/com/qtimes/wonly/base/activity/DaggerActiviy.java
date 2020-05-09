package com.qtimes.wonly.base.activity;

import android.os.Bundle;

import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.dagger.base.BaseDagger;
import com.qtimes.wonly.dagger.base.DaggerDelegate;
import com.qtimes.wonly.dagger.component.ActivityComponent;
import com.qtimes.wonly.dagger.component.CommonActivityComponent;
import com.qtimes.wonly.dagger.modules.ActivityModule;

import androidx.annotation.NonNull;

/**
 * 实现了daggeer2的activity
 * author: liutao
 * date: 2016/6/22.
 */
public abstract class DaggerActiviy<C extends BaseComponent> extends BaseActivity implements BaseDagger<C, ActivityComponent> {
    C component;
    CommonActivityComponent commonActivityComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initInject();
        initFirst();
        super.onCreate(savedInstanceState);
    }

    protected void initFirst() {
    }

    //dagger依赖注入实现
    public void initInject() {
        ActivityComponent activityComponent = DaggerDelegate.provideActivityComponent(new ActivityModule(this));
        component = initComponent(activityComponent);
    }

    public CommonActivityComponent initCommon() {
        commonActivityComponent = DaggerDelegate.provideActivityComponent(new ActivityModule(this)).provideCommonComponent();
        return commonActivityComponent;
    }

    @NonNull
    @Override
    public C initComponent(@NonNull ActivityComponent component) {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commonActivityComponent = null;
        component = null;
    }
}