package com.qtimes.domain.base;

import rx.Observable;
import rx.Subscriber;

/**
 * author: liutao
 * date: 2016/8/26.
 */
public interface UseCase<R extends BaseReqParameter,C extends BaseCallback,T> {

    Observable<T> buildObservable(R reqParameter, C callback);

    Observable.Transformer<T, T> buildTransformer();

    Subscriber<T> buildSubscriber(R reqParameter, C callback);

    void  release();

    void execute(R reqParameter, C callback);
}
