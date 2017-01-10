package com.liudong.douban.di.components;

import com.liudong.douban.di.modules.ActivityModule;
import com.liudong.douban.di.scopes.PerActivity;
import com.liudong.douban.ui.activity.BaseActivity;
import com.liudong.douban.ui.activity.CollectActivity;
import com.liudong.douban.ui.activity.EditProfileActivity;
import com.liudong.douban.ui.activity.MainActivity;
import com.liudong.douban.ui.activity.MemberActivity;
import com.liudong.douban.ui.activity.MovieDetailActivity;
import com.liudong.douban.ui.fragment.BaseFragment;
import com.liudong.douban.ui.fragment.LazyFragment;
import com.liudong.douban.ui.fragment.login.LoginFragment;
import com.liudong.douban.ui.fragment.login.RegisterFragment;
import com.liudong.douban.ui.fragment.movie.HotMFragment;
import com.liudong.douban.ui.fragment.movie.TopMFragment;
import com.liudong.douban.ui.fragment.movie.UpcomingMFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(BaseActivity baseActivity);

    void inject(MainActivity mainActivity);

    void inject(BaseFragment baseFragment);

    void inject(LazyFragment lazyFragment);

    void inject(HotMFragment hotMFragment);

    void inject(TopMFragment topMFragment);

    void inject(UpcomingMFragment upcomingMFragment);

    void inject(MovieDetailActivity movieDetailActivity);

    void inject(LoginFragment loginFragment);

    void inject(RegisterFragment registerFragment);

    void inject(EditProfileActivity editProfileActivity);

    void inject(MemberActivity memberActivity);

    void inject(CollectActivity collectActivity);

}
