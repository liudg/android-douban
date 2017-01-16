package com.liudong.douban.ui.adapter.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static com.liudong.douban.ui.adapter.LoadMoreWrapper.ITEM_TYPE_LOAD_FAILED_VIEW;
import static com.liudong.douban.ui.adapter.LoadMoreWrapper.ITEM_TYPE_LOAD_MORE_VIEW;
import static com.liudong.douban.ui.adapter.LoadMoreWrapper.ITEM_TYPE_NO_MORE_VIEW;

/**
 * Created by liudong on 17-1-10.
 * RecyclerView的item点击事件
 */

public abstract class OnItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;
    private boolean mIsPrePress = false;
    private boolean mIsShowPress = false;
    private View mPressedView = null;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (recyclerView == null) {
            this.recyclerView = rv;
            mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener(recyclerView));
        }
        //处理长按事件
        if (!mGestureDetector.onTouchEvent(e) && e.getActionMasked() == MotionEvent.ACTION_UP && mIsShowPress) {
            if (mPressedView != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(mPressedView);
                if (vh == null || !isFooterView(vh.getItemViewType())) {
                    mPressedView.setPressed(false);
                }
            }
            mIsPrePress = false;
            mIsShowPress = false;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        private RecyclerView recyclerView;

        ItemTouchHelperGestureListener(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mIsPrePress = true;
            mPressedView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            super.onDown(e);
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if (mIsPrePress && mPressedView != null) {
                mIsShowPress = true;
            }
            super.onShowPress(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mIsPrePress && mPressedView != null) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    return false;
                }
                final View pressedView = mPressedView;
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(pressedView);

                if (isFooterView(vh.getItemViewType())) {
                    return false;
                }
                mPressedView.setPressed(true);
                onItemClick(vh.getLayoutPosition());
                resetPressedView(pressedView);
            }
            return true;
        }

        private void resetPressedView(final View pressedView) {
            if (pressedView != null) {
                pressedView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pressedView != null) {
                            pressedView.setPressed(false);
                        }
                    }
                }, 100);
            }

            mIsPrePress = false;
            mPressedView = null;
        }
    }

    private boolean isFooterView(int type) {
        return (type == ITEM_TYPE_LOAD_FAILED_VIEW || type == ITEM_TYPE_NO_MORE_VIEW || type == ITEM_TYPE_LOAD_MORE_VIEW);
    }

    public abstract void onItemClick(int position);
}
