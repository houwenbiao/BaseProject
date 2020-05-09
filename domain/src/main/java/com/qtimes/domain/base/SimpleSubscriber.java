package com.qtimes.domain.base;

import rx.Subscriber;

/**
 * Created by liutao on 2017/1/3.
 */

public class SimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }
}
