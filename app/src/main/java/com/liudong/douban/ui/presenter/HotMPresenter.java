package com.liudong.douban.ui.presenter;

import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.model.movie.MovieList;
import com.liudong.douban.di.scopes.PerActivity;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@PerActivity
public class HotMPresenter extends Presenter<HotMPresenter.View> {

    private View view;
    private final DataManager mDataManager;

    @Inject
    HotMPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(View view) {
        fragmentLifecycle.onNext(FragmentEvent.CREATE_VIEW);
        this.view = view;
    }

    @Override
    public void detachView() {
        fragmentLifecycle.onNext(FragmentEvent.DESTROY_VIEW);
        view = null;
    }

    public void loadData(final int start, int count) {
        mDataManager.loadHotMovies(start, count)
                .compose(this.<MovieList>bindFragmentEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieList>() {
                    @Override
                    public void onCompleted() {
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
