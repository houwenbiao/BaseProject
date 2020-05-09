package com.qtimes.pavilion.base.layout;

/**
 * Created by liutao on 2017/2/25.
 */

public interface BaseView {
    /**
     * 当自定义view实现此方法时,调用改方法释放相应的资源
     * 通常在BaseActivity或者baseFragment中释放
     *
     *
     */
    void release();

    /**
     * 判断资源释放已经释放，防止重复调用释放方法
     * 调用release之前需要先调用此方法进行判断
     * @return
     */
    boolean isRelease();
}
