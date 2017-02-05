package com.liudong.douban.ui.presenter;

import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.model.user.MovieCollect;
import com.liudong.douban.di.scopes.PerActivity;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

@PerActivity
public class CollectMPresenter extends Presenter<CollectMPresenter.View> {

    private View view;
    private final DataManager dataManager;

    @Inject
    CollectMPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        unSubscribe();
        view = null;
    }

    public void loadData(int start) {
        BmobQuery<MovieCollect> bmobQuery = new BmobQuery<>();
        addSubscription(bmobQuery.setLimit(10).setSkip(start).order("-createdAt")
                .addWhereEqualTo("user", BmobUser.getCurrentUser().getUsername())
                .findObjects(new FindListener<MovieCollect>() {
                    @Override
                    public void done(List<MovieCollect> list, BmobException e) {
                        if (e == null) {
                            view.showMovieCollect(list);
                        } else {
                            view.showMessage("获取收藏列表失败" + e.getMessage());
                        }
                    }
                }));
    }

    public interface View {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void showMovieCollect(List<MovieCollect> movieCollect);
    }
}
