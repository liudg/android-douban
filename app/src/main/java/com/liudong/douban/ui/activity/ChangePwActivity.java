package com.liudong.douban.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.widget.EditText;

import com.liudong.douban.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

public class ChangePwActivity extends BaseActivity {

    @BindView(R.id.et_oldPw)
    EditText etOldPw;
    @BindView(R.id.layout_oldPw)
    TextInputLayout layoutOldPw;
    @BindView(R.id.et_newPw)
    EditText etNewPw;
    @BindView(R.id.layout_newPw)
    TextInputLayout layoutNewPw;

    private Subscription subscription;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("修改密码");
    }

    @OnClick(R.id.btn_change)
    void change() {
        String oldPw = etOldPw.getText().toString().trim();
        String newPw = etNewPw.getText().toString().trim();
        if (oldPw.isEmpty()) {
            layoutOldPw.setError("密码不能为空");
            return;
        }
        layoutOldPw.setErrorEnabled(false);
        if (newPw.isEmpty() || newPw.length() < 6) {
            layoutNewPw.setError("密码长度必须大于6");
            return;
        }
        layoutNewPw.setErrorEnabled(false);
        subscription = BmobUser.updateCurrentUserPassword(oldPw, newPw, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    hideProgressDialog();
                    ChangePwActivity.this.finish();
                    showToast("密码修改成功，可以用新密码进行登录啦");
                } else {
                    hideProgressDialog();
                    showToast("失败：" + e.getMessage());
                }
            }
        });
        showProgressDialog();
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在执行...");
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
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
        return R.layout.activity_change_pw;
    }
}
