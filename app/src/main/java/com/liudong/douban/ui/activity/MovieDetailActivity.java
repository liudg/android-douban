package com.liudong.douban.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liudong.douban.R;
import com.liudong.douban.data.model.movie.Actors;
import com.liudong.douban.data.model.movie.MovieDetail;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.ui.adapter.StaggeredGridAdapter;
import com.liudong.douban.ui.presenter.DetailMPresenter;
import com.liudong.douban.ui.view.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MovieDetailActivity extends BaseActivity implements DetailMPresenter.View {

    @Inject
    DetailMPresenter detailMPresenter;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.tv_rating)
    TextView tv_rating;
    @BindView(R.id.rb_start)
    MaterialRatingBar rb_start;
    @BindView(R.id.tv_wish)
    TextView tv_wish;
    @BindView(R.id.etv_intro)
    ExpandableTextView etv_intro;
    @BindView(R.id.rv_actors)
    RecyclerView rv_actors;

    @OnClick(R.id.fab)
    void collect() {
        showToast("点击收藏");
    }

    private MovieDetail movieData;
    private int isLoad = 1;
    private MenuItem refreshItem;

    private String id;
    private StaggeredGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailMPresenter.attachView(this);
        rv_actors.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mAdapter = new StaggeredGridAdapter(this);
        rv_actors.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        Bundle data = getIntent().getBundleExtra("data");
        Glide.with(this)
                .load(data.getString("imgUrl"))
                .into(iv_avatar);
        id = data.getString("id");
        getSupportActionBar().setTitle(data.getString("title"));
        detailMPresenter.loadData(Integer.parseInt(id));
    }

    private void refreshData() {
        String intro = "";
        for (String genre : movieData.genres()) {
            intro += genre + "/";
        }
        intro = intro.substring(0, intro.length() - 1);
        tv_info.setText("原名：" + movieData.original_title() +
                "\n国家：" + movieData.countries() +
                "\n导演：" + movieData.directors().get(0).name() +
                "\n上映时间：" + movieData.year() +
                "\n类型：" + intro);
        tv_rating.setText(String.valueOf(movieData.rating().average()));
        rb_start.setNumStars(movieData.rating().max() / 2);
        rb_start.setRating((float) (movieData.rating().average() / 2));
        tv_wish.setText("" + movieData.collect_count() + "人");
        etv_intro.setText(movieData.summary());
        List<Actors> actor = new ArrayList<>();
        actor.addAll(movieData.directors());
        actor.addAll(movieData.casts());
        mAdapter.setData(actor);
    }

    @Override
    protected void onDestroy() {
        detailMPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_movie_detail;
    }

    @Override
    public void showProgress() {
        isLoad = 1;
        invalidateOptionsMenu();
    }

    @Override
    public void hideProgress() {
        isLoad = 0;
        invalidateOptionsMenu();
    }

    @Override
    public void showMessage(String message) {
        hideProgress();
        Log.e("MovieDetailActivity", message);
        showToast(getString(R.string.load_failed));
    }

    @Override
    public void showMovieDetail(MovieDetail movieDetail) {
        movieData = movieDetail;
        refreshData();
        hideProgress();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (isLoad) {
            case 0:
                hideRefreshAnimation();
                break;
            case 1:
                refreshMenuItemView(menu.findItem(R.id.action_refresh));
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_share:
                return true;
            case R.id.action_refresh:
                if (isLoad == 1) {
                    return true;
                }
                detailMPresenter.loadData(Integer.parseInt(id));
                isLoad = 1;
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    private void refreshMenuItemView(MenuItem item) {
        refreshItem = item;
        ImageView refreshView = (ImageView) getLayoutInflater().inflate(R.layout.rotate_imageview, null);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_ic_refresh);
        animation.setRepeatCount(Animation.INFINITE);
        refreshView.setAnimation(animation);
        refreshItem.setActionView(refreshView);
    }

    private void hideRefreshAnimation() {
        if (refreshItem != null) {
            View view = refreshItem.getActionView();
            if (view != null) {
                view.clearAnimation();
                refreshItem.setActionView(null);
            }
        }
    }
}
