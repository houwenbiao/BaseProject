package com.qtimes.wonly.base.mvp;

/**
 * Created by liuj on 2016/6/20.
 */
public interface SimpleView extends MvpView {
    void onSuccess();
    void onError(Throwable throwable);
}
