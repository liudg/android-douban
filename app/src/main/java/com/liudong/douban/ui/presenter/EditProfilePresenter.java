package com.liudong.douban.ui.presenter;

import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.model.user.Person;
import com.liudong.douban.di.scopes.PerActivity;

import java.io.File;

import javax.inject.Inject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by liudong on 2016/12/29.
 * 用户信息编辑业务类
 */
@PerActivity
public class EditProfilePresenter extends Presenter<EditProfilePresenter.View> {

    private View view;
    private final DataManager mDataManager;

    @Inject
    EditProfilePresenter(DataManager dataManager) {
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

    /**
     * 更新用户操作并同步更新本地的用户信息
     */
    public void updateUser(String imgUrl, final String name, final String sex, final String dec) {
        final Person person = BmobUser.getCurrentUser(Person.class);
        if (person != null) {
            if (imgUrl != null) {
                final BmobFile bmobFile = new BmobFile(new File(imgUrl));
                addSubscription(bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            startUpdate(person, bmobFile.getFileUrl(), name, sex, dec);
                        } else {
                            view.showMessage("头像上传失败");
                        }
                    }
                }));
            } else {
                startUpdate(person, imgUrl, name, sex, dec);
            }
        } else {
            view.showMessage("本地用户为空，请登录。");
        }
    }

    private void startUpdate(Person person, String imgUrl, String name, String sex, String dec) {
        final Person newPerson = new Person();
        newPerson.setSex(sex);
        newPerson.setDescription(dec);
        if (!name.equals(person.getUsername())) {
            newPerson.setUsername(name);
        }
        if (imgUrl != null) {
            newPerson.setPicture(imgUrl);
        }
        addSubscription(newPerson.update(person.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    view.succeed();
                } else {
                    view.showMessage(e.getMessage());
                }
            }
        }));
    }

    public interface View {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void succeed();
    }
}
