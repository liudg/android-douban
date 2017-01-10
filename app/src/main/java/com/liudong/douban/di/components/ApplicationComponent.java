package com.liudong.douban.di.components;

import android.content.Context;

import com.liudong.douban.MyApplication;
import com.liudong.douban.data.DataManager;
import com.liudong.douban.di.modules.ApplicationModule;
import com.liudong.douban.di.scopes.ApplicationContext;
import com.liudong.douban.event.RxBus;
import com.liudong.douban.utils.ToastUtil;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApplication application);

    /**
     * 给子类暴露实例
     */
    @ApplicationContext
    Context context();

    MyApplication mApplication();

    ToastUtil toastUtil();

    DataManager dataManager();

    RxBus rxBus();
}
