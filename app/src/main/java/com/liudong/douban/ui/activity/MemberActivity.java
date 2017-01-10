package com.liudong.douban.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liudong.douban.R;
import com.liudong.douban.data.DataManager;
import com.liudong.douban.data.model.user.Person;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.event.LogoutEvent;
import com.liudong.douban.event.RxBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MemberActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_dec)
    TextView tvDec;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @Inject
    DataManager dataManager;
    @Inject
    RxBus rxBus;

    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
        queryCollect();
        eventBus();
    }

    private void initData() {
        Person person = BmobUser.getCurrentUser(Person.class);
        if (person != null) {
            String username = person.getUsername();
            String imgUrl = person.getPicture();
            String sex = person.getSex();
            String dec = person.getDescription();
            getSupportActionBar().setTitle(username);
            if (imgUrl != null) {
                Glide.with(this)
                        .load(imgUrl)
                        .into(ivAvatar);
            }
            if (sex != null && sex.equals("女")) {
                ivSex.setImageResource(R.mipmap.ic_profile_female);
            } else {
                ivSex.setImageResource(R.mipmap.ic_profile_male);
            }
            if (dec != null) {
                tvDec.setText(dec);
            } else {
                tvDec.setText("这个人很懒，什么都没有留下");
            }
        }
    }

    /**
     * 查询收藏数量
     */
    private void queryCollect() {
        addSubscription(dataManager.getDataBaseHelper().queryCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        tvCount.setText("电影:" + integer);
                    }
                }));
    }

    private void eventBus() {
        addSubscription(rxBus.filteredObservable(Person.class)
                .subscribe(new Action1<Person>() {
                    @Override
                    public void call(Person person) {
                        //捕捉异常，防止发生错误时调用error方法导致订阅取消，后面事务无法接收
                        try {
                            String username = person.getUsername();
                            String imgUrl = person.getPicture();
                            String sex = person.getSex();
                            String dec = person.getDescription();
                            getSupportActionBar().setTitle(username);
                            if (imgUrl != null) {
                                Glide.with(MemberActivity.this)
                                        .load(imgUrl)
                                        .into(ivAvatar);
                            }
                            if (sex != null && sex.equals("女")) {
                                ivSex.setImageResource(R.mipmap.ic_profile_female);
                            } else {
                                ivSex.setImageResource(R.mipmap.ic_profile_male);
                            }
                            if (dec != null) {
                                tvDec.setText(dec);
                            } else {
                                tvDec.setText("这个人很懒，什么都没有留下");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    @OnClick(R.id.rl_collect)
    void toCollect() {
        Intent intent = new Intent(this, CollectActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_history)
    void toHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_change)
    void changePw() {
        Intent intent = new Intent(this, ChangePwActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注销");
        builder.setMessage("确认退出登录吗？");
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BmobUser.logOut();
                dataManager.getDataBaseHelper().deleteAllMovie();
                LogoutEvent logoutEvent = new LogoutEvent(getString(R.string.article), getString(R.string.gmail), false);
                rxBus.post(logoutEvent);
                MemberActivity.this.finish();
            }
        });
        builder.show();
    }

    /**
     * 解决Subscription内存泄露问题
     *
     * @param s
     */
    private void addSubscription(Subscription s) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("data", "memberActivity");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_member;
    }
}
