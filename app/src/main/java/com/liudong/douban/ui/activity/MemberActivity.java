package com.liudong.douban.ui.activity;

import android.os.Bundle;

import com.liudong.douban.R;

public class MemberActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("个人中心");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_member;
    }
}
