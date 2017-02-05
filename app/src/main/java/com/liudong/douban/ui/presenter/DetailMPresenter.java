package com.liudong.douban.ui.presenter;

import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.model.movie.MovieDetail;
import com.liudong.douban.data.model.user.MovieCollect;
import com.liudong.douban.data.model.user.MovieCollectDb;
import com.liudong.douban.di.scopes.PerActivity;

import javax.inject.Inject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

@PerActivity
public class DetailMPresenter extends Presenter<DetailMPresenter.View> {

    private View view;
    private final DataManager mDataManager;
    private BmobUser bmobUser;
    private String objectId;

    @Inject
    DetailMPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(View view) {
        activityLifecycle.onNext(ActivityEvent.CREATE);
        this.view = view;
        bmobUser = BmobUser.getCurrentUser();
    }

    @Override
    public void detachView() {
        activityLifecycle.onNext(ActivityEvent.DESTROY);
        unSubscribe();
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

    public void collect(MovieDetail movieDetail) {
        final String user;
        final String id = movieDetail.id();
        String imgUrl = movieDetail.images().medium();
        String title = movieDetail.title();
        String year = movieDetail.year();
        String actor = movieDetail.directors().get(0).name();
        if (bmobUser != null) {
            user = bmobUser.getUsername();
            MovieCollect movieCollect = new MovieCollect();
            movieCollect.setId(id);
            movieCollect.setUser(user);
            movieCollect.setImgUrl(imgUrl);
            movieCollect.setTitle(title);
            movieCollect.setYear(year);
            movieCollect.setActor(actor);
            addSubscription(movieCollect.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        MovieCollectDb data = MovieCollectDb.builder()
                                .objectId(s).mid(id).build();
                        mDataManager.getDataBaseHelper().insertMovie(data);
                    } else {
                        view.collectMessage("收藏失败");
                    }
                }
            }));
        } else {
            view.collectMessage("用户未登录");
        }
    }

    public void cancelCollect(String id) {
        MovieCollect movieCollect = new MovieCollect();
        movieCollect.setObjectId(objectId);
        addSubscription(movieCollect.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mDataManager.getDataBaseHelper().deleteMovie(objectId);
                    view.collectMessage("成功");
                } else {
                    view.collectMessage("取消收藏失败");
                }
            }
        }));
    }

    public void isCollect(String id) {
        mDataManager.getDataBaseHelper().queryMovie(id)
                .compose(this.<MovieCollectDb>bindActivityEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MovieCollectDb>() {
                    @Override
                    public void call(MovieCollectDb movieCollectDb) {
                        if (movieCollectDb != null) {
                            view.collectState(true);
                            objectId = movieCollectDb.objectId();
                        } else {
                            view.collectState(false);
                        }
                    }
                });
    }

    public interface View {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void showMovieDetail(MovieDetail movieDetail);

        void collectMessage(String message);

        void collectState(Boolean state);
    }
}
