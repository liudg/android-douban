package com.liudong.douban.data;

import com.liudong.douban.data.local.DataBaseHelper;
import com.liudong.douban.data.local.PreferencesHelper;
import com.liudong.douban.data.model.movie.MovieDetail;
import com.liudong.douban.data.model.movie.MovieList;
import com.liudong.douban.data.remote.DouBanService;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by liudong on 2016/11/25.
 * 统一管理数据源，对上层屏蔽底层细节
 */
@Singleton
public class DataManager {

    private final DouBanService mDouBanService;
    private final DataBaseHelper mDataBaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    DataManager(DouBanService douBanService, DataBaseHelper dataBaseHelper, PreferencesHelper preferencesHelper) {
        mDouBanService = douBanService;
        mDataBaseHelper = dataBaseHelper;
        mPreferencesHelper = preferencesHelper;
        System.out.println("1111");
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public DataBaseHelper getDataBaseHelper() {
        return mDataBaseHelper;
    }

    public Observable<MovieList> loadHotMovies(int start, int count) {
        return mDouBanService.getHotMovie(start, count);
    }

    public Observable<MovieList> loadTopMovies(int start, int count) {
        return mDouBanService.getTopMovie(start, count);
    }

    public Observable<MovieList> loadUpMovies(int start, int count) {
        return mDouBanService.getUpcomingMovie(start, count);
    }

    public Observable<MovieDetail> loadMovieDetail(int id) {
        return mDouBanService.getMovieDetail(id);
    }
}