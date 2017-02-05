package com.liudong.douban.ui.presenter;

import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.model.user.Person;
import com.liudong.douban.di.scopes.PerActivity;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

@PerActivity
public class RegisterPresenter extends Presenter<RegisterPresenter.View> {

    private View view;
    private final DataManager mDataManager;

    private String number;
    private String password;

    @Inject
    RegisterPresenter(DataManager dataManager) {
        mDataManager = dataManager;
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

    public EventHandler getEventHandler() {
        return new EventHandler() {
            @Override
            public void afterEvent(int event, int result, final Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        register();
                        SMSSDK.unregisterAllEventHandler();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    view.showMessage(((Throwable) data).getMessage());
                }
            }
        };
    }

    private void register() {
        Person person = new Person();
        person.setUsername("逗瓣_" + number);
        person.setPassword(password);
        person.setMobilePhoneNumber(number);
        addSubscription(person.signUp(new SaveListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if (e == null) {
                    view.succeed();
                } else {
                    view.showMessage(e.getMessage());
                }
            }
        }));
    }

    public void setInfo(String num, String pw) {
        number = num;
        password = pw;
    }

    public interface View {

        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void succeed();
    }
}
