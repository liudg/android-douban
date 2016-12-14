package com.liudong.douban.ui.fragment.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liudong.douban.R;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.ui.activity.LoginActivity;
import com.liudong.douban.ui.activity.MemberActivity;
import com.liudong.douban.ui.fragment.BaseFragment;
import com.liudong.douban.ui.presenter.RegisterPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterFragment extends BaseFragment implements RegisterPresenter.View {

    @BindView(R.id.layout_number)
    TextInputLayout layout_number;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.layout_password)
    TextInputLayout layout_password;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.layout_auth)
    TextInputLayout layout_auth;
    @BindView(R.id.et_auth)
    EditText et_auth;

    @Inject
    RegisterPresenter registerPresenter;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerPresenter.attachView(this);
        initSms();
    }

    private void initSms() {
        String appKey = "19d41ba529a14";
        String appSecret = "985d1b294a6a9d328a5a8f8f20d34cce";
        SMSSDK.initSDK(getContext(), appKey, appSecret);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, final Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        layout_auth.post(new Runnable() {
                            @Override
                            public void run() {
                                layout_auth.setErrorEnabled(false);
                            }
                        });
                        registerPresenter.register();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((LoginActivity) getActivity()).hidePopWindows();
                            showToast(data.toString());
                        }
                    });
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    @OnClick(R.id.register)
    public void register() {
        String number = et_name.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String auth = et_auth.getText().toString().trim();
        if (number.isEmpty() || !isChinaPhoneLegal(number)) {
            layout_number.setError("手机号码不正确");
            return;
        }
        layout_number.setErrorEnabled(false);
        if (password.isEmpty() || password.length() < 6) {
            layout_password.setError("密码长度不能小于6位");
            return;
        }
        layout_password.setErrorEnabled(false);
        if (auth.isEmpty()) {
            layout_auth.setError("验证码错误");
            return;
        }
        SMSSDK.submitVerificationCode("86", number, auth);
        ((LoginActivity) getActivity()).showPopWindows();
    }

    @OnClick(R.id.bt_send)
    public void sendVCode(final Button button) {
        final String number = et_name.getText().toString().trim();
        if (number.isEmpty()) {
            showToast("手机号码不能为空");
            return;
        }
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                button.setEnabled(false);
                button.setTextColor(Color.BLUE);
                button.setText("" + l / 1000 + "秒后重新发送");
            }

            @Override
            public void onFinish() {
                button.setEnabled(true);
                button.setTextColor(Color.WHITE);
                button.setText("发送验证码");
            }
        }.start();
        SMSSDK.getVerificationCode("86", number);
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showMessage(String message) {
        Log.e("RegisterFragment", message);
        layout_auth.post(new Runnable() {
            @Override
            public void run() {
                ((LoginActivity) getActivity()).hidePopWindows();
                showToast(getString(R.string.load_failed));
            }
        });
    }

    @Override
    public void succeed() {
        getActivity().startActivity(new Intent(getContext(), MemberActivity.class));
        layout_auth.post(new Runnable() {
            @Override
            public void run() {
                showToast("注册成功");
            }
        });
        getActivity().finish();
    }

    public boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerPresenter.detachView();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_register;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
