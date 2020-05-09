package com.qtimes.pavilion.base.mvp;

/**
 * Created by liuj on 2016/7/21.
 */

public interface MvpLoadingView extends MvpView {
    void onLoadCompleted();

    void onLoading();
}
