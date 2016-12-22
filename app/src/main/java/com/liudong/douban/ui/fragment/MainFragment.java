package com.liudong.douban.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 主页面承载ViewPager的Fragment
 * 集中管理生命周期 重启时恢复数据
 */
public abstract class MainFragment extends Fragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private static final String STATE_SAVE_TOOLBAR_TIT = "STATE_SAVE_TOOLBAR_TIT";
    private boolean isHidden;
    private int toolbarTit;

    private Unbinder unbinder;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            toolbarTit = savedInstanceState.getInt(STATE_SAVE_TOOLBAR_TIT);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (isHidden) {
                transaction.hide(this);
            } else {
                transaction.show(this);
            }
            transaction.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getFragmentLayout(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        if (!isHidden && savedInstanceState != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarTit);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
        outState.putInt(STATE_SAVE_TOOLBAR_TIT, getFragmentTit());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public View getView() {
        return view;
    }

    //加载布局
    protected abstract int getFragmentLayout();

    //Activity重启时恢复toolbar标题
    protected abstract int getFragmentTit();
}
