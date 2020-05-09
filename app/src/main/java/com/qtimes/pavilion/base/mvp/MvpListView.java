package com.qtimes.pavilion.base.mvp;

import java.util.List;

/**
 * 列表数据
 * Created by liuj on 2016/6/20.
 * 流列表
 */
public interface MvpListView<T> extends MvpStatusView {

    /**
     * 加载成功
     * @param dataList
     * @param isReload 是否重新加载，一般第一次加载未true
     */
    void onLoadSuccess(List<T> dataList, boolean isReload);

    /**
     * 加载失败
     *
     * @param cache
     * @param throwable
     * @param isReload
     */
    void onLoadError(List<T> cache, Throwable throwable, boolean isReload);

    /**
     * 是否有更多
     *
     * @param hasMore
     */
    void setHasMore(boolean hasMore);

    /**
     * 获取每页条数
     *
     * @return
     */
    int getPageSize();

}
