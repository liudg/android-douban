package com.liudong.douban.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.ui.activity.BaseActivity;
import com.liudong.douban.utils.ToastUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 懒加载Fragment
 * 用于ViewPager中的fragment，当页面可见时才加载数据
 */
public abstract class LazyFragment extends Fragment {

    @Inject
    ToastUtil toastUtil;

    private Unbinder unbinder;
    private View view;

    //当前fragment是否可见
    protected boolean isVisible;
    //view是否初始化完成
    protected boolean isInitView;
    //是否加载过数据
    protected boolean isLoadData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDagger(((BaseActivity) getActivity()).activityComponent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getFragmentLayout(), container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInitView = true;
        //解决一开始加载的时候未调用lazyLoadData()，因为setUserVisibleHint比onActivityCreated触发前
        if (getUserVisibleHint()) {
            lazyLoadData();
        }
    }

    protected void lazyLoadData() {
        if (!isInitView || !isVisible || isLoadData) {
            return;
        }
        initData();
        isLoadData = true;
    }

    public View getView() {
        return view;
    }

    /**
     * 需要支持Dagger2的子页面重写这个方法
     */
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /**
     * 显示Toast
     */
    protected void showToast(String text) {
        toastUtil.showToast(text);
    }

    /**
     * 加载布局文件
     */
    protected abstract int getFragmentLayout();

    /**
     * 这里获取数据，刷新界面
     */
    protected abstract void initData();

}
