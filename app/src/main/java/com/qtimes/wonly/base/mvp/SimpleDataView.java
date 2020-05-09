package com.qtimes.wonly.base.mvp;

/**
 * Created by liuj on 2016/6/20.
 */
public interface SimpleDataView<T> extends MvpView {
    void onSuccess(T data);

    void onError(Throwable throwable);
}
