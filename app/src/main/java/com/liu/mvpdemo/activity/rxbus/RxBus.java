package com.liu.mvpdemo.activity.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author liuxuhui
 * @date 2019/1/25
 */
public class RxBus {

    private PublishSubject<Object> rxBus = PublishSubject.create();

    public RxBus() {
    }

    public void send(Object object) {
        rxBus.onNext(object);
    }

    public Observable<Object> toObservable() {
        return rxBus;
    }

    public boolean hasObservables() {
        return rxBus.hasObservers();
    }
}
