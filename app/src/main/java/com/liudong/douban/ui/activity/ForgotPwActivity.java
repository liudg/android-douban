package com.liudong.douban.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.liudong.douban.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ForgotPwActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.layout_name)
    TextInputLayout layoutName;
    @BindView(R.id.et_newPw)
    EditText etNewPw;
    @BindView(R.id.layout_newPw)
    TextInputLayout layoutNewPw;
    @BindView(R.id.et_auth)
    EditText etAuth;
    @BindView(R.id.layout_auth)
    TextInputLayout layoutAuth;

    private CompositeSubscription compositeSubscription;
    private ProgressDialog progressDialog;
    private String number;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("忘记密码");
    }

    private void initSms() {
        String appKey = "19d41ba529a14";
        String appSecret = "985d1b294a6a9d328a5a8f8f20d34cce";
        SMSSDK.initSDK(this, appKey, appSecret);
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, final Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        SMSSDK.unregisterAllEventHandler();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    etAuth.post(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                            showToast("验证码提交失败：" + ((Throwable) data).getMessage());
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.bt_send)
    public void send(final Button button) {
        initSms();
        number = etName.getText().toString().trim();
        if (number.isEmpty() && !isChinaPhoneLegal(number)) {
            layoutName.setError("手机号码错误");
            return;
        }
        layoutName.setErrorEnabled(false);
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
        etName.postDelayed(new Runnable() {
            @Override
            public void run() {
                SMSSDK.getVerificationCode("86", number);
            }
        }, 1000);
    }

    @OnClick(R.id.btn_change)
    public void change() {
        password = etNewPw.getText().toString().trim();
        String auth = etAuth.getText().toString().trim();
        if (password.isEmpty() || password.length() < 6) {
            layoutNewPw.setError("密码格式错误");
            return;
        }
        layoutNewPw.setErrorEnabled(false);
        if (auth.isEmpty()) {
            layoutAuth.setError("验证码不能为空");
            return;
        }
        layoutAuth.setErrorEnabled(false);
        SMSSDK.submitVerificationCode("86", number, auth);
        showProgressDialog();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在执行...");
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private void addSubscription(Subscription s) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(s);
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
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_forgot_pw;
    }
}
