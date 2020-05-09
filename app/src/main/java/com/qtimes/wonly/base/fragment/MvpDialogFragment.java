package com.qtimes.wonly.base.fragment;
import android.os.Bundle;

import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.dagger.base.BaseComponent;

import com.qtimes.wonly.base.mvp.MvpPresenter;

/**
 * Created by plu on 2016/9/2.
 */

public abstract class MvpDialogFragment<C extends BaseComponent, P extends MvpPresenter> extends DaggerDialogFragment<C> {
    protected P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            PluLog.e("Presenter被销毁了");
            mPresenter.detachView();
        }
    }
}
