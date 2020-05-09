package com.qtimes.pavilion.base.fragment;

import androidx.annotation.LayoutRes;
import com.qtimes.pavilion.dagger.base.BaseComponent;

import android.view.View;

import com.qtimes.views.CommonContainer;

import com.qtimes.pavilion.R;
import com.qtimes.pavilion.base.activity.StatusView;
import com.qtimes.pavilion.base.mvp.MvpPresenter;
import com.qtimes.pavilion.base.mvp.MvpStatusView;

/**
 * 管理fragment的一些状态
 * author: liutao
 * date: 2016/6/14.
 */
public abstract class MvpStatusFragment<C extends BaseComponent, P extends MvpPresenter> extends MvpFragment<C,P> implements MvpStatusView,StatusView,CommonContainer.CommonView{
    protected CommonContainer fragmentContainer;
    @Override
    protected void initView(View view) {
        super.initView(view);
        fragmentContainer= (CommonContainer) view.findViewById(R.id.viewContainer);
        if(fragmentContainer!=null){
            fragmentContainer.setCommonView(this);
        }
    }





    @Override
    public void showContent() {
        fragmentContainer.setStatus(CommonContainer.Status.DEFAULT);
    }

    @Override
    public void showError(boolean reload) {
        if(reload){
            fragmentContainer.setStatus(CommonContainer.Status.ERROR);
        }
    }

    @Override
    public void showEmpty(boolean hideAll) {
        fragmentContainer.setStatus(hideAll, CommonContainer.Status.EMPTY);
    }

    @Override
    public void showLoading(boolean reload) {
        if(reload){
            fragmentContainer.setStatus(CommonContainer.Status.LOADING);
        }
    }



    @Override
    public void onCompleted() {
        fragmentContainer.hideLoading();
    }
    @Override
    public void onLoading(boolean reload) {
        showLoading(reload);
    }

    @Override
    public void setErrorView(@LayoutRes int res) {
        fragmentContainer.setErrorView(res);
    }



    @Override
    public void setLoadingView(@LayoutRes int res) {
        fragmentContainer.setLoadingView(res);
    }

    @Override
    public void onErrorClick(View view) {
    }

    @Override
    public void setEmptyView(@LayoutRes int res) {
        fragmentContainer.setEmptyView(res);
    }

}
