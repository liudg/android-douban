package com.liudong.douban.ui.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.liudong.douban.R;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.ui.fragment.BaseFragment;

public class LoginFragment extends BaseFragment {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
