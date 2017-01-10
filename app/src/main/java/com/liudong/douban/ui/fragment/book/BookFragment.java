package com.liudong.douban.ui.fragment.book;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.liudong.douban.R;
import com.liudong.douban.ui.fragment.MainFragment;

import butterknife.BindView;

public class BookFragment extends MainFragment {

    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    public static BookFragment newInstance() {
        return new BookFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
    }

    private void initViewPager() {
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_content;
    }

    @Override
    public int getFragmentTit() {
        return R.string.book_tit;
    }
}
