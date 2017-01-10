package com.liudong.douban.ui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by liudong on 2016/12/20.
 * Desc:用于RecyclerView加载更多的监听，实现滑动到底部自动加载更多
 */
abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal;
    private boolean isLoading = true;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = lm.getItemCount();
        int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
        if (isLoading) {
            if (totalItemCount > previousTotal) {//加载更多结束
                isLoading = false;
                previousTotal = totalItemCount;
            } else if (totalItemCount < previousTotal) {//用户刷新结束
                previousTotal = totalItemCount;
                isLoading = false;
            } else {
                isLoading = true;
            }
        }
        if (!isLoading && visibleItemCount > 0 && totalItemCount - 1 == lastVisibleItemPosition && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            isLoading = true;
            loadMore();
        }
    }

    public abstract void loadMore();
}
