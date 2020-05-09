package com.qtimes.wonly.base.fragment;

import android.os.Bundle;

import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.dagger.base.BaseDagger;
import com.qtimes.wonly.dagger.component.CommonFragmentComponent;
import com.qtimes.wonly.dagger.component.FragmentComponent;
import com.qtimes.wonly.dagger.modules.FragmentModule;

import androidx.annotation.NonNull;

import com.qtimes.wonly.app.App;

/**
 * 实现了dagger2注入的fragment
 * author: liutao
 * date: 2016/6/22.
 */
public abstract class DaggerFragment<C extends BaseComponent> extends BaseFragment  implements BaseDagger<C, FragmentComponent> {
    protected C component;
    CommonFragmentComponent commonFragmentComponent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        initInject();
        super.onCreate(savedInstanceState);
    }
    public void initInject() {
        FragmentComponent fragmentComponent = App.getInstance().getApplicationComponent().provideFragmentComponent(new FragmentModule(this));
        component= initComponent(fragmentComponent);
    }
    public CommonFragmentComponent initCommon() {
        commonFragmentComponent= App.getInstance().getApplicationComponent().provideFragmentComponent(new FragmentModule(this)).provideCommonComponent();
        return commonFragmentComponent;
    }
    @Override
    public C initComponent(@NonNull FragmentComponent component) {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        commonFragmentComponent=null;
        component=null;
    }
}
