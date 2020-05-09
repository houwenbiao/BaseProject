package com.qtimes.wonly.base.activity;

import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.base.mvp.MvpPresenter;
import com.qtimes.wonly.base.mvp.MvpView;


/**
 * mvpActivity
 * Created by lt on 2016/5/10.
 */
public abstract class MvpActivity<C extends BaseComponent, P extends MvpPresenter> extends DaggerActiviy<C> implements MvpView
{
    protected P presenter;

    @Override
    protected void initFirst()
    {
        presenter = createPresenter();
    }

    public abstract P createPresenter();

    @Override
    protected void onDestroy()
    {
        if (presenter != null)
        {
            presenter.detachView();
        }
        super.onDestroy();
    }
}