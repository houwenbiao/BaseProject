package com.qtimes.wonly.base.activity;

import androidx.annotation.LayoutRes;
import com.qtimes.wonly.dagger.base.BaseComponent;

import android.view.View;

import com.qtimes.utils.android.PluLog;
import com.qtimes.views.CommonContainer;

import com.qtimes.wonly.R;
import com.qtimes.wonly.base.mvp.MvpPresenter;
import com.qtimes.wonly.base.mvp.MvpStatusView;

/**
 * 自带title的activity，
 * 注意：子类继承此Activity,
 * 布局文件如果有
 * CommonTainer ,id必须为viewContainer
 * Created by liutao on 2016/6/8.
 */
public abstract class MvpStatusActivity<C extends BaseComponent, P extends MvpPresenter> extends MvpActivity<C, P> implements MvpStatusView, StatusView, CommonContainer.CommonView {

    protected CommonContainer activityContainer;

    @Override
    protected void initView() {
        initActivityContainer();
    }

    protected void initActivityContainer() {
        activityContainer = (CommonContainer) findViewById(R.id.viewContainer);
        if (activityContainer != null) {
            activityContainer.setCommonView(this);
        }
    }

    @Override
    public void showContent() {
        activityContainer.setStatus(CommonContainer.Status.DEFAULT);
    }

    public void hideAll() {
        activityContainer.hideAllView();
    }

    @Override
    public void showError(boolean reload) {
        if (reload) {
            activityContainer.setStatus(CommonContainer.Status.ERROR);
        }
    }

    @Override
    public void showEmpty(boolean hideAll) {
        activityContainer.setStatus(hideAll, CommonContainer.Status.EMPTY);
    }

    @Override
    public void showLoading(boolean reload) {
        PluLog.e(reload);
        if (reload) {
            activityContainer.setStatus(CommonContainer.Status.LOADING);
        }
    }


    @Override
    public void onCompleted() {
        activityContainer.hideLoading();
    }

    @Override
    public void onLoading(boolean reload) {
        showLoading(reload);
    }


    @Override
    public void setErrorView(@LayoutRes int res) {
        activityContainer.setErrorView(res);
    }


    @Override
    public void setLoadingView(@LayoutRes int res) {
        activityContainer.setLoadingView(res);
    }


    @Override
    public void setEmptyView(@LayoutRes int res) {
        activityContainer.setEmptyView(res);
    }

    @Override
    public void onErrorClick(View view) {

    }
}
