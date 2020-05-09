package com.qtimes.views.recyclerview;

import android.view.View;

/**
 * Created by liuj on 2016/3/7.
 */
public abstract class LoadMorePolicy<T extends View> {

    protected T mScrollableView;
    protected int mPageSize; //每页加载的数量
    protected float mRatio; //预加载比例
    protected OnLoadMoreListener onLoadMoreListener;
    protected boolean mHasMore;

    public LoadMorePolicy(T scrollableView, int pageSize, float ratio) {
        mScrollableView = scrollableView;
        mPageSize = pageSize;
        mRatio = ratio;
        mHasMore = true;
        init();
    }

    protected abstract void init();


    public boolean hasMore() {
        return mHasMore;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();

        void scrollToBottom();
    }

    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
    }
}
