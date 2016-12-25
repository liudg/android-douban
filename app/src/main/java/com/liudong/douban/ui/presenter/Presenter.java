package com.liudong.douban.ui.presenter;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * Created by liudong on 2016/11/16.
 * 基础Presenter
 */

abstract class Presenter<T> {

    public abstract void attachView(T view);

    public abstract void detachView();

    /**
     * RxFragment生命周期
     */
    enum FragmentEvent {
        ATTACH,
        CREATE,
        CREATE_VIEW,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY_VIEW,
        DESTROY,
        DETACH
    }

    final BehaviorSubject<FragmentEvent> fragmentLifecycle = BehaviorSubject.create();

    <F> Observable.Transformer<F, F> bindFragmentEvent(final FragmentEvent bindEvent) {
        return new Observable.Transformer<F, F>() {
            @Override
            public Observable<F> call(Observable<F> observable) {
                return observable.takeUntil(fragmentLifecycle.takeFirst(new Func1<FragmentEvent, Boolean>() {
                    @Override
                    public Boolean call(FragmentEvent event) {
                        return event.equals(bindEvent);
                    }
                }));
            }
        };
    }


    /**
     * RxActivity生命周期
     */
    enum ActivityEvent {
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    final BehaviorSubject<ActivityEvent> activityLifecycle = BehaviorSubject.create();

    <A> Observable.Transformer<A, A> bindActivityEvent(final ActivityEvent bindEvent) {
        return new Observable.Transformer<A, A>() {
            @Override
            public Observable<A> call(Observable<A> observable) {
                return observable.takeUntil(activityLifecycle.takeFirst(new Func1<ActivityEvent, Boolean>() {
                    @Override
                    public Boolean call(ActivityEvent event) {
                        return event.equals(bindEvent);
                    }
                }));
            }
        };
    }
}
