package com.qtimes.wonly.base.fragment;

import android.os.Bundle;

import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.base.mvp.MvpPresenter;

/**
 * 简单MVP模式使用
 * Created by lt on 2016/5/10.
 */
public abstract class MvpFragment<C extends BaseComponent, P extends MvpPresenter> extends DaggerFragment<C> {
    protected P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
