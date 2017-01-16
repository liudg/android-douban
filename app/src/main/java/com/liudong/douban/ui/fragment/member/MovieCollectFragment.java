package com.liudong.douban.ui.fragment.member;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.liudong.douban.R;
import com.liudong.douban.data.model.user.MovieCollect;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.ui.activity.MovieDetailActivity;
import com.liudong.douban.ui.adapter.CollectAdapter;
import com.liudong.douban.ui.adapter.LoadMoreWrapper;
import com.liudong.douban.ui.adapter.listener.OnItemClickListener;
import com.liudong.douban.ui.fragment.LazyFragment;
import com.liudong.douban.ui.presenter.CollectMPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MovieCollectFragment extends LazyFragment implements CollectMPresenter.View,
        SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @Inject
    CollectMPresenter collectMPresenter;

    CollectAdapter mAdapter;
    LoadMoreWrapper loadMoreWrapper;
    List<MovieCollect> collects;

    private int start;
    private int count;
    private boolean isLoad;

    public static MovieCollectFragment newInstance() {
        return new MovieCollectFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        collectMPresenter.attachView(this);
        collects = new ArrayList<>();
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new CollectAdapter(this);
        loadMoreWrapper = new LoadMoreWrapper(mAdapter);
        loadMoreWrapper.setContext(getContext());
        loadMoreWrapper.setOnLoadListener(this);
        recyclerView.setAdapter(loadMoreWrapper);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MovieCollect collect = collects.get(position);
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("imgUrl", collect.getImgUrl());
                bundle.putString("title", collect.getTitle());
                bundle.putString("id", collect.getId());
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (!isLoad) {
            start = 0;
            collectMPresenter.loadData(start);
            loadMoreWrapper.showLoadMore();
            isLoad = true;
        } else {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onRetry() {
        collectMPresenter.loadData(start);
        isLoad = true;
    }

    @Override
    public void onLoadMore() {
        if (!isLoad) {
            if (count < 10) {
                loadMoreWrapper.showLoadComplete();
                isLoad = false;
            } else {
                collectMPresenter.loadData(start);
                isLoad = true;
            }
        }
    }

    @Override
    protected void initData() {
        start = 0;
        isLoad = true;
        collectMPresenter.loadData(start);
        showProgress();
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        isLoad = false;
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        hideProgress();
        loadMoreWrapper.showLoadError();
        showToast(message);
    }

    @Override
    public void showMovieCollect(List<MovieCollect> movieCollect) {
        count = movieCollect.size();
        if (start == 0) {
            mAdapter.setDate(start, movieCollect);
            collects.clear();
            collects.addAll(movieCollect);
            loadMoreWrapper.notifyDataSetChanged();
        } else {
            mAdapter.setDate(start, movieCollect);
            collects.addAll(movieCollect);
            loadMoreWrapper.notifyItemInserted(start);
        }
        start += count;
        hideProgress();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_collect_movie;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void onDestroyView() {
        collectMPresenter.detachView();
        super.onDestroyView();
    }
}
