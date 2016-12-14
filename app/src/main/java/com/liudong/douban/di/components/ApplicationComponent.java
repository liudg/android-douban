package com.liudong.douban.di.components;

import android.content.Context;

import com.liudong.douban.MyApplication;
import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.local.DataBaseHelper;
import com.liudong.douban.data.local.PreferencesHelper;
import com.liudong.douban.data.remote.DouBanService;
import com.liudong.douban.di.modules.ApplicationModule;
import com.liudong.douban.di.scopes.ApplicationContext;
import com.liudong.douban.utils.ToastUtil;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by liudong on 2016/11/22.
 * Class note:
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApplication application);

    /**
     * 给子类暴露实例
     */
    @ApplicationContext
    Context context();

    ToastUtil toastUtil();

    PreferencesHelper preferencesHelper();

    DataBaseHelper databaseHelper();

    DataManager dataManager();

    DouBanService douBanService();
}
