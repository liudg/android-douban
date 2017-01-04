package com.liudong.douban.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liudong.douban.R;
import com.liudong.douban.data.model.user.Person;
import com.liudong.douban.event.RxBus;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;

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
    @BindView(R.id.btn_change)
    Button btnChange;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    private Subscription rxSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
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

    private void eventBus() {
        rxSubscription = RxBus.getInstance().tObservable(Person.class)
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
                });
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
        if (!rxSubscription.isUnsubscribed()) {
            rxSubscription.unsubscribe();
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_member;
    }
}
