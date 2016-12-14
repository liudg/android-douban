package com.liudong.douban.ui.presenter;

import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.model.movie.MovieList;
import com.liudong.douban.di.scopes.PerActivity;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liudong on 2016/11/16.
 * class note:
 */
@PerActivity
public class UpcomingMPresenter implements Presenter<UpcomingMPresenter.View> {

    private View view;
    private final DataManager mDataManager;

    @Inject
    UpcomingMPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void loadData(int start, int count) {
        mDataManager.loadUpMovies(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieList>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieList movieList) {
                        view.showMovie(movieList);
                    }
                });
    }

    public interface View {

        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void showMovie(MovieList movieList);
    }
}
