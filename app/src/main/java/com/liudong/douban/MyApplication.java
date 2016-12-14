package com.liudong.douban;

import android.app.Application;

import com.liudong.douban.di.components.ApplicationComponent;
import com.liudong.douban.di.components.DaggerApplicationComponent;
import com.liudong.douban.di.modules.ApplicationModule;

/**
 * Created by liudong on 2016/11/22.
 * class note:
 * Base Application for Application
 */

public class MyApplication extends Application {

    ApplicationComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ApplicationComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerApplicationComponent.builder().
                    applicationModule(new ApplicationModule(this)).build();
        }
        return mAppComponent;
    }
}
