package com.liudong.douban.ui.presenter;

import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.model.movie.MovieDetail;
import com.liudong.douban.di.scopes.PerActivity;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@PerActivity
public class DetailMPresenter extends Presenter<DetailMPresenter.View> {

    private View view;
    private final DataManager mDataManager;

    @Inject
    public DetailMPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(View view) {
        activityLifecycle.onNext(ActivityEvent.CREATE);
        this.view = view;
    }

    @Override
    public void detachView() {
        activityLifecycle.onNext(ActivityEvent.DESTROY);
        view = null;
    }

    public void loadData(int id) {
        mDataManager.loadMovieDetail(id)
                .compose(this.<MovieDetail>bindActivityEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetail>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieDetail movieDetail) {
                        view.showMovieDetail(movieDetail);
                    }
                });
    }

    public interface View {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void showMovieDetail(MovieDetail movieDetail);
    }
}
