package com.liudong.douban.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liudong.douban.MyApplication;
import com.liudong.douban.R;
import com.liudong.douban.ui.adapter.listener.EndLessOnScrollListener;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

/**
 * 为RecyclerView添加加载更多（装饰者模式）
 */
public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_LOAD_FAILED_VIEW = 0x00000111;
    public static final int ITEM_TYPE_NO_MORE_VIEW = 0x00000222;
    public static final int ITEM_TYPE_LOAD_MORE_VIEW = 0x00000333;

    private Context mContext;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

    private View mLoadMoreView;
    private View mLoadMoreFailedView;
    private View mNoMoreView;

    private int mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
    private EndLessOnScrollListener loadMoreScrollListener;

    public LoadMoreWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mInnerAdapter = adapter;
        loadMoreScrollListener = new EndLessOnScrollListener() {
            @Override
            public void loadMore() {
                if (mOnLoadListener != null) {
                    mOnLoadListener.onLoadMore();
                }
            }
        };
    }

    public void showLoadMore() {
        mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
        notifyItemChanged(getItemCount() - 1);
    }

    public void showLoadError() {
        mCurrentItemType = ITEM_TYPE_LOAD_FAILED_VIEW;
        notifyItemChanged(getItemCount() - 1);
    }

    public void showLoadComplete() {
        mCurrentItemType = ITEM_TYPE_NO_MORE_VIEW;
        notifyItemChanged(getItemCount() - 1);
    }

    private ViewHolder getLoadMoreViewHolder(ViewGroup parent) {
        if (mLoadMoreView == null) {
            mLoadMoreView = LayoutInflater.from(mContext).inflate(R.layout.load_more, parent, false);
        }
        return ViewHolder.createViewHolder(mLoadMoreView);
    }

    private ViewHolder getLoadFailedViewHolder() {
        if (mLoadMoreFailedView == null) {
            mLoadMoreFailedView = new TextView(mContext);
            mLoadMoreFailedView.setPadding(16, 24, 16, 32);
            mLoadMoreFailedView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            ((TextView) mLoadMoreFailedView).setText("加载失败，请点我重试");
            ((TextView) mLoadMoreFailedView).setGravity(Gravity.CENTER);
        }
        return ViewHolder.createViewHolder(mLoadMoreFailedView);
    }

    private ViewHolder getNoMoreViewHolder() {
        if (mNoMoreView == null) {
            mNoMoreView = new TextView(mContext);
            mNoMoreView.setPadding(16, 24, 16, 32);
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
        switch (viewType) {
            case ITEM_TYPE_NO_MORE_VIEW:
                return getNoMoreViewHolder();
            case ITEM_TYPE_LOAD_MORE_VIEW:
                return getLoadMoreViewHolder(parent);
            case ITEM_TYPE_LOAD_FAILED_VIEW:
                return getLoadFailedViewHolder();
            default:
                return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_TYPE_LOAD_FAILED_VIEW:
                mLoadMoreFailedView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MyApplication.getInstance().isConnected()) {
                            Toast.makeText(mContext, R.string.load_failed, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (mOnLoadListener != null) {
                            showLoadMore();
                            mOnLoadListener.onRetry();
                        }
                    }
                });
                return;
            case ITEM_TYPE_LOAD_MORE_VIEW:
                return;
            case ITEM_TYPE_NO_MORE_VIEW:
                return;
            default:
                mInnerAdapter.onBindViewHolder(holder, position);
        }
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
            ViewGroup mParent = (ViewGroup) itemView.getParent();
            if (mParent != null) {
                mParent.removeView(itemView);
            }
            return new ViewHolder(itemView);
        }
    }

    public void setContext(Context context) {
        mContext = context;
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
