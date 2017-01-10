package com.liudong.douban.data.remote.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.liudong.douban.MyApplication;

/**
 * Created by liudong on 2016/12/24.
 * 监听网络变化
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkReceiver";
    private MyApplication mApplication;

    public NetworkConnectChangedReceiver(MyApplication application) {
        mApplication = application;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //监听网络连接的设置，包括wifi和移动数据的打开与关闭
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                mApplication.setConnected(true);
                Log.i(TAG, "当前网络连接可用");
            } else {
                mApplication.setConnected(false);
                Log.e(TAG, "当前没有网络连接，请确保你已经打开网络");
            }
        }
    }
}
