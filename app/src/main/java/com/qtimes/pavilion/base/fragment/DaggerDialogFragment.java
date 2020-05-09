package com.qtimes.pavilion.base.fragment;

import android.os.Bundle;

import com.qtimes.pavilion.dagger.base.BaseComponent;
import com.qtimes.pavilion.dagger.base.BaseDagger;
import com.qtimes.pavilion.dagger.component.CommonFragmentComponent;
import com.qtimes.pavilion.dagger.component.FragmentComponent;
import com.qtimes.pavilion.dagger.modules.FragmentModule;

import androidx.annotation.NonNull;

import com.qtimes.pavilion.app.App;

/**
 * Created by plu on 2016/9/2.
 */

public abstract class DaggerDialogFragment<C extends BaseComponent> extends BaseDialogFragment implements
        BaseDagger<C, FragmentComponent> {

    protected C component;
    CommonFragmentComponent commonFragmentComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initInject();
        super.onCreate(savedInstanceState);
    }

    public void initInject() {
        FragmentComponent fragmentComponent = App.getInstance()
                                                 .getApplicationComponent()
                                                 .provideFragmentComponent(new FragmentModule(this));
        component = initComponent(fragmentComponent);
    }

    public CommonFragmentComponent initCommon() {
        commonFragmentComponent = App.getInstance()
                                     .getApplicationComponent()
                                     .provideFragmentComponent(new FragmentModule(this))
                                     .provideCommonComponent();
        return commonFragmentComponent;
    }

    @Override
    public C initComponent(@NonNull FragmentComponent component) {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        commonFragmentComponent = null;
        component = null;
    }
}
