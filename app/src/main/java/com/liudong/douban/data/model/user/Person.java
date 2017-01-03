package com.liudong.douban.data.model.user;

import cn.bmob.v3.BmobUser;

/**
 * Created by liudong on 2016/12/27.
 * 注册用户信息，BmobUser默认有用户名、密码等属性
 */

public class Person extends BmobUser {
    private static final long serialVersionUID = 1L;
    private String description;
    private String sex;
    private String picture;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
