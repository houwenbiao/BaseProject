package com.qtimes.wonly.base.activity;

/**
 * activity和fragment 对View的控制接口
 * author: liutao
 * date: 2016/6/14.
 */
public interface StatusView {
    void showLoading(boolean reload);
    void showContent();
    void showError(boolean reload);
    void showEmpty(boolean hideAll);
}
