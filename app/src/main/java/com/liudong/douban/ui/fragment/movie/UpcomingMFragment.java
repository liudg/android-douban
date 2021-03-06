package com.liudong.douban.ui.fragment.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.liudong.douban.R;
import com.liudong.douban.data.model.movie.MovieList;
import com.liudong.douban.data.model.movie.Subjects;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.ui.activity.MovieDetailActivity;
import com.liudong.douban.ui.adapter.LoadMoreWrapper;
import com.liudong.douban.ui.adapter.RecyclerViewAdapter;
import com.liudong.douban.ui.adapter.listener.OnItemClickListener;
import com.liudong.douban.ui.fragment.LazyFragment;
import com.liudong.douban.ui.presenter.UpcomingMPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class UpcomingMFragment extends LazyFragment implements UpcomingMPresenter.View,
        SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadListener {

    @Inject
    UpcomingMPresenter upcomingMPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(this);
    LoadMoreWrapper loadMoreWrapper = new LoadMoreWrapper(mAdapter);
    List<Subjects> movieData = new ArrayList<>();

    private int start;
    private int count;
    private int total;
    private boolean isLoad;

    public static UpcomingMFragment newInstance() {
        return new UpcomingMFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        upcomingMPresenter.attachView(this);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        loadMoreWrapper.setContext(getContext());
        loadMoreWrapper.setOnLoadListener(this);
        recyclerView.setAdapter(loadMoreWrapper);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Subjects subjects = movieData.get(position);
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("imgUrl", subjects.images().large());
                bundle.putString("title", subjects.title());
                bundle.putString("id", subjects.id());
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if (!isLoad) {
            loadMoreWrapper.showLoadMore();
            upcomingMPresenter.loadData(0, 20);
            isLoad = true;
        } else {
            swipeRefresh.setRefreshing(false);
        }
    }

    /**
     * 重新加载
     */
    @Override
    public void onRetry() {
        int num = start + count;
        upcomingMPresenter.loadData(num, count);
        isLoad = true;
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        int num = start + count;
        if (num < total) {
            if (!isLoad) {
                upcomingMPresenter.loadData(num, count);
                isLoad = true;
            }
        } else {
            loadMoreWrapper.showLoadComplete();
        }
    }

    @Override
    public void onDestroyView() {
        if (isLoad) {
            loadMoreWrapper.showLoadError();
        }
        upcomingMPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_tab_movie;
    }

    @Override
    protected void initData() {
        swipeRefresh.setRefreshing(true);
        upcomingMPresenter.loadData(0, 20);
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
        Log.e("UpcomingMFragment", message);
        showToast(getString(R.string.load_failed));
    }

    @Override
    public void showMovie(MovieList movieList) {
        start = movieList.start();
        count = movieList.count();
        total = movieList.total();
        mAdapter.setDate(start, movieList.subjects());
        if (start == 0) {
            movieData.clear();
            movieData.addAll(movieList.subjects());
            loadMoreWrapper.notifyDataSetChanged();
        } else {
            movieData.addAll(movieList.subjects());
            loadMoreWrapper.notifyItemInserted(start);
        }
        hideProgress();
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
