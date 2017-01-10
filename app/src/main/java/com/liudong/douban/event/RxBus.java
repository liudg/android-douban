package com.liudong.douban.event;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by liudong on 2017/1/4.
 * RxJava事件总线
 */
@Singleton
public class RxBus {

    /**
     * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
     */
    private final PublishSubject<Object> bus;

    @Inject
    public RxBus() {
        bus = PublishSubject.create();
    }

    /**
     * 发送一个新事件
     */
    public void post(Object event) {
        bus.onNext(event);
    }

    /**
     * 返回事件总线的所有观察者
     */
    public Observable<Object> observable() {
        return bus;
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> filteredObservable(final Class<T> eventClass) {
        return bus.ofType(eventClass);
    }
}
