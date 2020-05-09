package com.qtimes.domain.base;
import com.qtimes.domain.repository.DataRepository;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * author: liutao
 * date: 2016/8/26.
 */
public abstract class BaseUseCase<D extends DataRepository,R extends BaseReqParameter,C extends BaseCallback,T> implements UseCase<R,C,T> {
    protected D dataRepository;
    protected CompositeSubscription mCompositeSubscription;
    public BaseUseCase(D dataRepository) {
        this.dataRepository=dataRepository;
        mCompositeSubscription = new CompositeSubscription();

    }
    protected void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }


    @Override
    public Observable.Transformer<T, T> buildTransformer() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void release() {
        if (mCompositeSubscription == null||mCompositeSubscription.isUnsubscribed()) {
            return;
        }
        mCompositeSubscription.unsubscribe();
        mCompositeSubscription = null;
    }

    @Override
    public void execute(R req, C callback) {
        execute(buildTransformer(),req,callback);
    }
    public void execute(Observable.Transformer<T,T> transformer,R req, C callback) {
        Subscription subscriber = buildObservable(req,callback).compose(transformer).subscribe(buildSubscriber(req,callback));
        addSubscription(subscriber);
    }

}
