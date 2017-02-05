package com.liudong.douban.event;

/**
 * Created by liudong on 17-1-16.
 * 用户收藏事件
 */

public class CollectEvent {
    private boolean isCancelCollect;

    public CollectEvent(boolean isCancelCollect) {
        this.isCancelCollect = isCancelCollect;
    }

    public boolean isCancelCollect() {
        return isCancelCollect;
    }
}
