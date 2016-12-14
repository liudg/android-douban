package com.liudong.douban.ui.presenter;

/**
 * Created by liudong on 2016/11/16.
 */

interface Presenter<T> {
    void attachView(T view);

    void detachView();
}
