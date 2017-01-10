package com.liudong.douban.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.liudong.douban.R;
import com.liudong.douban.ui.adapter.ViewPagerAdapter;
import com.liudong.douban.ui.fragment.BaseFragment;
import com.liudong.douban.ui.fragment.login.LoginFragment;
import com.liudong.douban.ui.fragment.login.RegisterFragment;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements BaseFragment.ProgressListener {

    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private ProgressDialog progressDialog;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("登录/注册");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginFragment = LoginFragment.newInstance();
        registerFragment = RegisterFragment.newInstance();
        loginFragment.setListener(this);
        registerFragment.setListener(this);
        initViewPager();
    }

    private void initViewPager() {
        SparseArrayCompat<Fragment> fragments = new SparseArrayCompat<>();
        fragments.append(0, loginFragment);
        fragments.append(1, registerFragment);
        String[] titles = {getString(R.string.login), getString(R.string.register)};

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        vpContent.setAdapter(adapter);
        tabContent.setTabMode(TabLayout.MODE_FIXED);
        tabContent.setupWithViewPager(vpContent);
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
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
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在执行...");
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
