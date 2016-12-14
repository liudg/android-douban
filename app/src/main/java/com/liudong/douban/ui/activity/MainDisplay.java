package com.liudong.douban.ui.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.liudong.douban.R;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by liudong on 2016/11/8.
 * NavigationView菜单栏目切换
 */

public class MainDisplay {

    private FragmentManager fm;

    @Inject
    MainDisplay(Activity activity) {
        fm = ((AppCompatActivity) activity).getSupportFragmentManager();
    }

    /**
     * 加载初始时展示的frgment
     */
    void loadRootFragment(Fragment fragment, String tag) {
        addFragmentToActivity(fragment, tag);
    }

    /**
     * 显示fragment
     */
    void showFragment(Fragment fragment) {
        hideFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

    /**
     * 隐藏fragment
     */
    private void hideFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        List<Fragment> fragments = fm.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    /**
     * 增加Fragment到Activity
     */
    void addFragmentToActivity(Fragment fragment, String tag) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_content, fragment, tag);
        transaction.commit();
    }
}
