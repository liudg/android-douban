package com.liudong.douban.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.liudong.douban.R;
import com.liudong.douban.data.model.user.Person;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.event.RxBus;
import com.liudong.douban.ui.presenter.EditProfilePresenter;
import com.liudong.douban.utils.FileUtil;
import com.yalantis.ucrop.UCrop;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity implements EditProfilePresenter.View {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.rb_men)
    RadioButton rbMen;
    @BindView(R.id.rb_women)
    RadioButton rbWomen;
    @BindView(R.id.et_dec)
    EditText etDec;
    @BindView(R.id.layout_name)
    TextInputLayout layoutName;
    @BindView(R.id.layout_dec)
    TextInputLayout layoutDec;
    @Inject
    EditProfilePresenter editProfilePresenter;
    @Inject
    RxBus rxBus;

    private String imgUrl;
    private Uri cameraUri;
    private ProgressDialog progressDialog;
    private final String items[] = {"拍照", "相册"};
    private static final int REQUEST_CODE_FROM_CAMERA = 0;
    private static final int REQUEST_CODE_FROM_ALBUM = 1;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("编辑个人信息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editProfilePresenter.attachView(this);
        data = getIntent().getStringExtra("data");
        if (data != null) {
            fillInProfile();
        }
    }

    private void fillInProfile() {
        Person person = BmobUser.getCurrentUser(Person.class);
        if (person.getPicture() != null) {
            Glide.with(this)
                    .load(person.getPicture())
                    .into(ivAvatar);
        }
        etName.setText(person.getUsername());
        etDec.setText(person.getDescription());
        if (person.getSex() != null && person.getSex().equals("女")) {
            rbWomen.setChecked(true);
        } else {
            rbMen.setChecked(true);
        }
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String dec = etDec.getText().toString().trim();
        String sex;
        if (rbMen.isChecked()) {
            sex = "男";
        } else {
            sex = "女";
        }
        if (name.isEmpty()) {
            layoutName.setError("用户名不能为空");
            return;
        }
        layoutName.setErrorEnabled(false);
        if (dec.isEmpty()) {
            layoutDec.setError("请写下自己的座右铭吧");
            return;
        }
        layoutDec.setErrorEnabled(false);
        editProfilePresenter.updateUser(imgUrl, name, sex, dec);
        showProgress();
    }

    @OnClick(R.id.iv_avatar)
    public void selectImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择头像");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0:
                        cameraUri = FileUtil.getCameraPictureUri(getApplicationContext());
                        Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                        startActivityForResult(intentCapture, REQUEST_CODE_FROM_CAMERA);
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在执行...");
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(final String message) {
        Log.e("EditProfileActivity", message);
        hideProgress();
        showToast("修改信息失败：" + message);
    }

    @Override
    public void succeed() {
        hideProgress();
        rxBus.post(BmobUser.getCurrentUser(Person.class));
        if (data != null) {
            showToast("修改信息成功");
            this.finish();
        } else {
            startActivity(new Intent(this, MemberActivity.class));
            this.finish();
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FROM_CAMERA:
                    initUCrop(cameraUri, FileUtil.getSavePictureUri(this));
                    break;
                case REQUEST_CODE_FROM_ALBUM:
                    initUCrop(data.getData(), FileUtil.getSavePictureUri(this));
                    break;
                case UCrop.REQUEST_CROP:
                    Uri resultUri = UCrop.getOutput(data);
                    ivAvatar.setImageURI(resultUri);
                    imgUrl = resultUri.getPath();
                    break;
                case UCrop.RESULT_ERROR:
                    Throwable error = UCrop.getError(data);
                    showToast("头像裁剪出错：" + error.getMessage());
                    break;
            }
        }
    }

    private void initUCrop(Uri sourceUri, Uri destinationUri) {
        if (destinationUri != null) {
            UCrop uCrop = UCrop.of(sourceUri, destinationUri);
            //初始化UCrop配置
            UCrop.Options options = new UCrop.Options();
            options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
            uCrop.withOptions(options);
            uCrop.withAspectRatio(1, 1);
            uCrop.start(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_save:
                saveProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
