package com.liudong.douban.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liudong.douban.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_LOAD_FAILED_VIEW = Integer.MAX_VALUE - 1;
    private static final int ITEM_TYPE_NO_MORE_VIEW = Integer.MAX_VALUE - 2;
    private static final int ITEM_TYPE_LOAD_MORE_VIEW = Integer.MAX_VALUE - 3;

    private RecyclerView.Adapter mInnerAdapter;

    private View mLoadMoreView;
    private View mLoadMoreFailedView;
    private View mNoMoreView;

    private int mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
    private RvLoadMoreScrollListener loadMoreScrollListener;

    private boolean isLoadError = false;
    private boolean isLoadComplete = false;

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
        loadMoreScrollListener = new RvLoadMoreScrollListener() {
            @Override
            public void loadMore() {
                if (mOnLoadListener != null) {
                    if (!isLoadError && !isLoadComplete) {
                        mOnLoadListener.onLoadMore();
                    }
                }
            }
        };
    }

    public void showLoadMore() {
        mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
        isLoadError = false;
        isLoadComplete = false;
    }

    public void showLoadError() {
        mCurrentItemType = ITEM_TYPE_LOAD_FAILED_VIEW;
        isLoadError = true;
        isLoadComplete = false;
    }

    public void showLoadComplete() {
        mCurrentItemType = ITEM_TYPE_NO_MORE_VIEW;
        isLoadError = false;
        isLoadComplete = true;
    }

    public void removeLoadMore() {
        notifyItemRemoved(getItemCount() - 1);
    }

    private ViewHolder getLoadMoreViewHolder(ViewGroup parent) {
        if (mLoadMoreView == null) {
            mLoadMoreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, parent, false);
        }
        return ViewHolder.createViewHolder(mLoadMoreView);
    }

    private ViewHolder getLoadFailedViewHolder(ViewGroup parent) {
        if (mLoadMoreFailedView == null) {
            mLoadMoreFailedView = new TextView(parent.getContext());
            mLoadMoreFailedView.setPadding(16, 16, 16, 16);
            mLoadMoreFailedView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            ((TextView) mLoadMoreFailedView).setText("加载失败，请点我重试");
            ((TextView) mLoadMoreFailedView).setGravity(Gravity.CENTER);
        }
        return ViewHolder.createViewHolder(mLoadMoreFailedView);
    }

    private ViewHolder getNoMoreViewHolder(ViewGroup parent) {
        if (mNoMoreView == null) {
            mNoMoreView = new TextView(parent.getContext());
            mNoMoreView.setPadding(16, 16, 16, 16);
            mNoMoreView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            ((TextView) mNoMoreView).setText("已加载全部");
            ((TextView) mNoMoreView).setGravity(Gravity.CENTER);
        }
        return ViewHolder.createViewHolder(mNoMoreView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return mCurrentItemType;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NO_MORE_VIEW) {
            return getNoMoreViewHolder(parent);
        } else if (viewType == ITEM_TYPE_LOAD_MORE_VIEW) {
            return getLoadMoreViewHolder(parent);
        } else if (viewType == ITEM_TYPE_LOAD_FAILED_VIEW) {
            return getLoadFailedViewHolder(parent);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_TYPE_LOAD_FAILED_VIEW) {
            mLoadMoreFailedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onRetry();
                        showLoadMore();
                    }
                }
            });
            return;
        } else if (holder.getItemViewType() == ITEM_TYPE_LOAD_MORE_VIEW) {
            return;
        } else if (holder.getItemViewType() == ITEM_TYPE_NO_MORE_VIEW) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(loadMoreScrollListener);
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() == 0 ? 0 : mInnerAdapter.getItemCount() + 1;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }

        static ViewHolder createViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }

        static ViewHolder createViewHolder(ViewGroup parent, int layoutId) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new ViewHolder(itemView);
        }
    }

    //加载监听
    public interface OnLoadListener {
        void onRetry();

        void onLoadMore();
    }

    private OnLoadListener mOnLoadListener;

    public LoadMoreWrapper setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
        return this;
    }
}
