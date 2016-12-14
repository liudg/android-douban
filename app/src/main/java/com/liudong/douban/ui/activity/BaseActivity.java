package com.liudong.douban.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.liudong.douban.MyApplication;
import com.liudong.douban.R;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.di.components.DaggerActivityComponent;
import com.liudong.douban.di.modules.ActivityModule;
import com.liudong.douban.utils.ToastUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ToastUtil toastUtil;

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewID());
        ButterKnife.bind(this);

        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        //Dagger2注入
        injectDagger(activityComponent());
    }

    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(((MyApplication) getApplication()).getAppComponent())
                    .build();
        }
        return mActivityComponent;
    }

    /**
     * 需要支持Dagger2的子页面重写这个方法
     */
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /**
     * 显示Toast
     */
    protected void showToast(String text) {
        toastUtil.showToast(text);
    }

    /**
     * 绑定布局文件
     */
    protected abstract int getContentViewID();
}
