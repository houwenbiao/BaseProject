package com.qtimes.views.recyclerview;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by liuj on 2016/2/25.
 */
public class RecyclerViewLoadMorePolicy extends LoadMorePolicy<RecyclerView> {
    private String TAG = RecyclerViewLoadMorePolicy.class.getName();

    private boolean isScrollUp;
    private LoadMoreAdapter adapter;

    public RecyclerViewLoadMorePolicy(RecyclerView recyclerView, int pageSize, float ratio) {
        super(recyclerView, pageSize, ratio);
        adapter = (LoadMoreAdapter) mScrollableView.getAdapter();
    }

    public void init() {

        mScrollableView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, newState + "");
                if (isScrollUp) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        if (recyclerView.getLayoutManager() == null || recyclerView.getLayoutManager().getItemCount() == 0) {
                            return;
                        }
                        int lastVisiblePos = 0;
                        int[] spanIndex;
                        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            lastVisiblePos = layoutManager.findLastCompletelyVisibleItemPosition();
                        } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                            spanIndex = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPositions(null);
                            lastVisiblePos = spanIndex[spanIndex.length - 1];
                        }
                        int totalCount = recyclerView.getLayoutManager().getItemCount();
                        if (lastVisiblePos != 0) {
                            int currentPage = totalCount / mPageSize - 1;
                            if (currentPage < 0) {
                                //第一页数据小于pageSize,返回不做操作
                                if (lastVisiblePos == totalCount - 1) {
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.scrollToBottom();
                                    }
                                }
                                return;
                            }

                            if (mHasMore) {
                                Log.e("mHasMore","更多");
                                if (lastVisiblePos >= (totalCount - (1 - mRatio) * mPageSize) - 1) {
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.onLoadMore();
                                    }
                                }
                            } else {
                                Log.e("mHasMore",mHasMore+"");
                                if (lastVisiblePos == totalCount - 1) {
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.scrollToBottom();
                                    }
                                }
                            }
                        }
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrollUp = dy > 0;
            }
        });

    }

    @Override
    public void setHasMore(boolean hasMore) {
        super.setHasMore(hasMore);
        if (hasMore) {
            adapter.showFooter();
        } else {
            adapter.hideFooter();
        }

    }


    public interface LoadMoreAdapter {

        int TYPE_FOOTER = 100; //footer viewType

        /**
         * 显示footer
         */
        void showFooter();

        /**
         * 隐藏footer
         */
        void hideFooter();


    }
}
