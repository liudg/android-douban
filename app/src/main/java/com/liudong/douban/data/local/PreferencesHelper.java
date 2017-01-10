package com.liudong.douban.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.liudong.douban.di.scopes.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "db_pref_file";
    private final SharedPreferences mPref;

    @Inject
    PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPref() {
        return mPref;
    }

    public void clear() {
        mPref.edit().clear().apply();
    }
}
