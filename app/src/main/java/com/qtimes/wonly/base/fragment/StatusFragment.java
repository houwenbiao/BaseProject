package com.qtimes.wonly.base.fragment;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.qtimes.views.CommonContainer;

import com.qtimes.wonly.R;
import com.qtimes.wonly.base.activity.StatusView;

/**
 * Created by liutao on 2016/8/19.
 */
public abstract class StatusFragment extends BaseFragment implements StatusView, CommonContainer.CommonView {
    protected CommonContainer fragmentContainer;

    @Override
    protected void initView(View view) {
        super.initView(view);
        fragmentContainer = (CommonContainer) view.findViewById(R.id.viewContainer);
        if (fragmentContainer != null) {
            fragmentContainer.setCommonView(this);
        }
    }


    @Override
    public void showContent() {
        fragmentContainer.setStatus(CommonContainer.Status.DEFAULT);
    }

    @Override
    public void showError(boolean reload) {
        if (reload) {
            fragmentContainer.setStatus(CommonContainer.Status.ERROR);
        }
    }

    @Override
    public void showEmpty(boolean hideAll) {
        fragmentContainer.setStatus(hideAll, CommonContainer.Status.EMPTY);
    }

    @Override
    public void showLoading(boolean reload) {
        if (reload) {
            fragmentContainer.setStatus(CommonContainer.Status.LOADING);
        }
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
