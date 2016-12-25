package com.liudong.douban.ui.presenter;

import com.liudong.douban.data.DataManager;
import com.liudong.douban.di.scopes.PerActivity;

import javax.inject.Inject;

@PerActivity
public class RegisterPresenter extends Presenter<RegisterPresenter.View> {

    private View view;
    private final DataManager mDataManager;

    @Inject
    RegisterPresenter(DataManager dataManager) {
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

    public void register() {
        view.succeed();
    }

    public interface View {

        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void succeed();
    }
}
