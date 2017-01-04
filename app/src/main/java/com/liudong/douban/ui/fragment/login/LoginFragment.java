package com.liudong.douban.ui.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.liudong.douban.R;
import com.liudong.douban.data.model.user.Person;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.event.RxBus;
import com.liudong.douban.ui.activity.LoginActivity;
import com.liudong.douban.ui.fragment.BaseFragment;
import com.liudong.douban.ui.presenter.LoginPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class LoginFragment extends BaseFragment implements LoginPresenter.View {

    @Inject
    LoginPresenter loginPresenter;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.layout_name)
    TextInputLayout layoutName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.layout_password)
    TextInputLayout layoutPassword;
    @BindView(R.id.tv_forgot)
    TextView tvForgot;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginPresenter.attachView(this);
    }

    @OnClick(R.id.login)
    public void login() {
        String nameOrNumber = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (nameOrNumber.isEmpty()) {
            layoutName.setError("用户名不能为空");
            return;
        }
        layoutName.setErrorEnabled(false);
        if (password.isEmpty() || password.length() < 6) {
            layoutPassword.setError("密码长度不能小于六位");
            return;
        }
        layoutPassword.setErrorEnabled(false);
        loginPresenter.login(nameOrNumber, password);
        showProgress();
    }

    @Override
    public void showProgress() {
        ((LoginActivity) getActivity()).showProgressDialog();
    }

    @Override
    public void hideProgress() {
        ((LoginActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        hideProgress();
        showToast("登录失败：" + message);
        Log.e("LoginFragment", message);
    }

    @Override
    public void succeed() {
        hideProgress();
        RxBus.getInstance().post(BmobUser.getCurrentUser(Person.class));
        getActivity().finish();
        showToast("登录成功");
    }

    @Override
    public void onDestroyView() {
        loginPresenter.detachView();
        super.onDestroyView();
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
