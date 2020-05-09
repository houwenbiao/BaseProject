package com.qtimes.domain.base;

/**
 * Created by liutao on 2017/1/13.
 */

public interface CommonCallBack<T>  extends BaseCallback{

    void onLoadDataSuccess(T t, boolean isReload);

    void onLoadDataError(Throwable e, boolean isReload);

}
