package com.qtimes.wonly.base.mvp;

/**
 * Created by liuj on 2016/7/21.
 */

public interface MvpLoadingView extends MvpView {
    void onLoadCompleted();

    void onLoading();
}
