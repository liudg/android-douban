package com.liudong.douban.di.modules;

import android.content.Context;

import com.liudong.douban.MyApplication;
import com.liudong.douban.data.remote.RetrofitConfig;
import com.liudong.douban.data.remote.DouBanService;
import com.liudong.douban.di.scopes.ApplicationContext;
import com.liudong.douban.utils.ToastUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liudong on 2016/11/22.
 * Application Module
 */
@Module
public class ApplicationModule {
    private final MyApplication mApplication;

    public ApplicationModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    MyApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    ToastUtil provideToastUtil(@ApplicationContext Context context) {
        return new ToastUtil(context);
    }

    @Provides
    @Singleton
    DouBanService provideDouBanService() {
        return RetrofitConfig.newDouBanService();
    }
}
