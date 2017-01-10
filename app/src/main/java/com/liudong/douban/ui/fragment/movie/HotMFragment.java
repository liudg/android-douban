package com.liudong.douban.ui.fragment.movie;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.liudong.douban.R;
import com.liudong.douban.data.model.movie.MovieList;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.ui.adapter.LoadMoreWrapper;
import com.liudong.douban.ui.adapter.RecyclerViewAdapter;
import com.liudong.douban.ui.fragment.LazyFragment;
import com.liudong.douban.ui.presenter.HotMPresenter;

import javax.inject.Inject;

import butterknife.BindView;

public class HotMFragment extends LazyFragment implements HotMPresenter.View {

    @Inject
    HotMPresenter hotMPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(this);
    LoadMoreWrapper loadMoreWrapper = new LoadMoreWrapper(mAdapter);

    private int start;
    private int count;
    private int total;
    private boolean isLoad;

    public static HotMFragment newInstance() {
        return new HotMFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hotMPresenter.attachView(this);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoad) {
                    loadMoreWrapper.showLoadMore();
                    hotMPresenter.loadData(0, 20);
                    isLoad = true;
                } else {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        loadMoreWrapper.setContext(getActivity());
        loadMoreWrapper.setOnLoadListener(new LoadMoreWrapper.OnLoadListener() {
            @Override
            public void onRetry() {
                int num = start + count;
                hotMPresenter.loadData(num, count);
                isLoad = true;
            }

            @Override
            public void onLoadMore() {
                int num = start + count;
                if (num < total) {
                    if (!isLoad) {
                        hotMPresenter.loadData(num, count);
                        isLoad = true;
                    }
                } else {
                    loadMoreWrapper.showLoadComplete();
                }
            }
        });
        recyclerView.setAdapter(loadMoreWrapper);
    }

    @Override
    public void onDestroyView() {
        if (isLoad) {
            loadMoreWrapper.showLoadError();
        }
        hotMPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_tab_movie;
    }

    @Override
    protected void initData() {
        swipeRefresh.setRefreshing(true);
        hotMPresenter.loadData(0, 20);
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
        isLoad = false;
    }

    @Override
    public void showMessage(String message) {
        hideProgress();
        loadMoreWrapper.showLoadError();
        Log.e("HotMFragment", message);
        showToast(getString(R.string.load_failed));
    }

    @Override
    public void showMovie(MovieList movieList) {
        start = movieList.start();
        count = movieList.count();
        total = movieList.total();
        mAdapter.setDate(start, movieList.subjects());
        if (start == 0) {
            loadMoreWrapper.notifyDataSetChanged();
        } else {
            loadMoreWrapper.notifyItemInserted(start);
        }
        hideProgress();
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
