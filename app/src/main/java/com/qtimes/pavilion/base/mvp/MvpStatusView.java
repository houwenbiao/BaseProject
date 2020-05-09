package com.qtimes.pavilion.base.mvp;

/**
 * author: liutao
 * date: 2016/6/14.
 */
public interface MvpStatusView extends MvpView{
    void onCompleted();
    /**
     *
     * @param reload 这个参数代表是否第一次加载，
     *               一般第一次加载的时候，回调的加载动画和下拉刷新的加载动画是不同的，用以区分
     */
    void onLoading(boolean reload);
}
