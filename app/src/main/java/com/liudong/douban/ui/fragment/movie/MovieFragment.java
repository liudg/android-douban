package com.liudong.douban.ui.fragment.movie;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.liudong.douban.R;
import com.liudong.douban.ui.adapter.ViewPagerAdapter;
import com.liudong.douban.ui.fragment.MainFragment;

import butterknife.BindView;

public class MovieFragment extends MainFragment {

    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    public static MovieFragment newInstance() {
        return new MovieFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
    }

    private void initViewPager() {
        SparseArrayCompat<Fragment> fragments = new SparseArrayCompat<>();
        fragments.append(0, HotMFragment.newInstance());
        fragments.append(1, TopMFragment.newInstance());
        fragments.append(2, UpcomingMFragment.newInstance());

        String[] titles = {
                getString(R.string.hot_movie),
                getString(R.string.top_movie),
                getString(R.string.upcoming_movie)
        };

        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments, titles);
        vpContent.setAdapter(mAdapter);

        tabContent.setTabMode(TabLayout.MODE_FIXED);
        tabContent.setupWithViewPager(vpContent);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_content;
    }

    @Override
    public int getFragmentTit() {
        return R.string.movie_tit;
    }
}
