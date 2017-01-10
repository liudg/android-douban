package com.liudong.douban.utils;

import android.content.Context;
import android.widget.Toast;

import com.liudong.douban.di.scopes.ApplicationContext;

/**
 * Created by liudong on 2016/11/23.
 * 统一管理Toast，防止一个页面多次弹出Toast造成阻塞
 */

public class ToastUtil {

    private Context mContext;
    private Toast mToast;

    public ToastUtil(@ApplicationContext Context context) {
        mContext = context;
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
}
