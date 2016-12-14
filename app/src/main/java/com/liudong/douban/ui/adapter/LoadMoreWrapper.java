package com.liudong.douban.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liudong.douban.R;

import butterknife.ButterKnife;

public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;

    public LoadMoreWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore() {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent, mLoadMoreLayoutId);
                mLoadMoreView = holder.view;
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() == 0 ? 0 : mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }

    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    public void restartLoad() {
        if (mLoadMoreView != null) {
            ProgressBar pb_load = ButterKnife.findById(mLoadMoreView, R.id.pb_load);
            TextView tv_load = ButterKnife.findById(mLoadMoreView, R.id.tv_load);
            pb_load.setVisibility(View.VISIBLE);
            tv_load.setText("正在加载");
        }
    }

    public void loadAllComplete() {
        ProgressBar pb_load = ButterKnife.findById(mLoadMoreView, R.id.pb_load);
        TextView tv_load = ButterKnife.findById(mLoadMoreView, R.id.tv_load);
        pb_load.setVisibility(View.GONE);
        tv_load.setText("已加载全部");
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        static ViewHolder createViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }

        static ViewHolder createViewHolder(ViewGroup parent, int layoutId) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new ViewHolder(itemView);
        }
    }
}
