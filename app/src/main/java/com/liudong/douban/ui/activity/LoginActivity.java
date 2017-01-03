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
import com.liudong.douban.ui.fragment.login.LoginFragment;
import com.liudong.douban.ui.fragment.login.RegisterFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("登录/注册");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViewPager();
    }

    private void initViewPager() {
        SparseArrayCompat<Fragment> fragments = new SparseArrayCompat<>();
        fragments.append(0, LoginFragment.newInstance());
        fragments.append(1, RegisterFragment.newInstance());
        String[] titles = {getString(R.string.login), getString(R.string.register)};

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        vpContent.setAdapter(adapter);
        tabContent.setTabMode(TabLayout.MODE_FIXED);
        tabContent.setupWithViewPager(vpContent);
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

    public boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
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
}
