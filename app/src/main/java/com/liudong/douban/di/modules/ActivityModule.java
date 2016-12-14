package com.liudong.douban.di.modules;

import android.app.Activity;
import android.content.Context;

import com.liudong.douban.di.scopes.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liudong on 2016/11/22.
 * Activity Module
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }
}
