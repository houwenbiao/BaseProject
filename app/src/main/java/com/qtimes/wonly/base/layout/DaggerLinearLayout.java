package com.qtimes.wonly.base.layout;

import android.content.Context;
import androidx.annotation.NonNull;
import com.qtimes.wonly.base.mvp.MvpPresenter;
import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.dagger.base.BaseDagger;
import com.qtimes.wonly.dagger.component.CommonLayoutComponent;
import com.qtimes.wonly.dagger.component.LayoutComponent;
import com.qtimes.wonly.dagger.modules.LayoutModule;

import android.util.AttributeSet;

import com.qtimes.wonly.app.App;
import com.qtimes.wonly.base.mvp.MvpView;

/**
 * Created by gufei on 2016/9/5 0005.
 */

public abstract class DaggerLinearLayout<C extends BaseComponent, V extends MvpView, P extends MvpPresenter<V>> extends BaseLinearLayout implements MvpView, BaseDagger<C, LayoutComponent> {
    protected P presenter;
    protected C component;
    protected CommonLayoutComponent commonLayoutComponent;

    public DaggerLinearLayout(Context context) {
        this(context, null);
    }

    public DaggerLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DaggerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void initData() {
        super.initData();
        initInject();
        presenter = createPresenter();
    }

    public abstract P createPresenter();

    //dagger依赖注入实现
    public void initInject() {
        LayoutComponent layoutComponent = App.getInstance().getApplicationComponent().provideLayoutComponent(new LayoutModule(this, getContext()));
        component = initComponent(layoutComponent);
    }

    public CommonLayoutComponent initCommon() {
        commonLayoutComponent =App.getInstance().getApplicationComponent().provideLayoutComponent(new LayoutModule(this, getContext())).provideCommonComponent();
        return commonLayoutComponent;
    }

    @NonNull
    @Override
    public C initComponent(@NonNull LayoutComponent component) {
        return null;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != presenter) {
            presenter.detachView();
            presenter = null;
        }
        if (null != commonLayoutComponent) {
            commonLayoutComponent = null;
        }
        if (null != component) {
            component = null;
        }
    }
}
