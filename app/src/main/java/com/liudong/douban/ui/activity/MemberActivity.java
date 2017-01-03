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

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
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
    protected int getContentViewID() {
        return R.layout.activity_member;
    }
}
