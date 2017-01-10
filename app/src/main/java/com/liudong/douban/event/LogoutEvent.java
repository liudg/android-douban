package com.liudong.douban.event;

/**
 * Created by liudong on 2017/1/4.
 * 用户注销事件注销
 */

public class LogoutEvent {
    private String username;
    private String dec;
    private boolean isLogin;

    public LogoutEvent(String username, String dec, boolean isLogin) {
        this.username = username;
        this.dec = dec;
        this.isLogin = isLogin;
    }

    public String getUsername() {
        return username;
    }

    public String getDec() {
        return dec;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
