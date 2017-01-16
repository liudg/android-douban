package com.liudong.douban.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.liudong.douban.R;
import com.liudong.douban.ui.adapter.ViewPagerAdapter;
import com.liudong.douban.ui.fragment.member.MovieCollectFragment;
import com.liudong.douban.ui.fragment.member.BookCollectFragment;

import butterknife.BindView;

public class CollectActivity extends BaseActivity {

    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("我的收藏");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViewPager();
    }

    private void initViewPager() {
        SparseArrayCompat<Fragment> fragments = new SparseArrayCompat<>();
        fragments.append(0, MovieCollectFragment.newInstance());
        fragments.append(1, BookCollectFragment.newInstance());
        String[] titles = {getString(R.string.movie), getString(R.string.book)};

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        vpContent.setAdapter(adapter);
        tabContent.setTabMode(TabLayout.MODE_FIXED);
        tabContent.setupWithViewPager(vpContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_collect;
    }
}
