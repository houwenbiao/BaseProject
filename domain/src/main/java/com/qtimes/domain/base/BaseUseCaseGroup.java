package com.qtimes.domain.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 组合usecase
 * author: liutao
 * date: 2016/8/26.
 */
public abstract class BaseUseCaseGroup<R extends BaseReqParameter, C extends BaseCallback> implements UseCaseGroup<R, C> {
    private Object[] releaseObjsect;
    protected C callback;
    protected R req;
    protected CompositeSubscription mCompositeSubscription;

    public BaseUseCaseGroup(Object... objects) {
        releaseObjsect = objects;
    }

    protected void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void release() {
        if (releaseObjsect == null || releaseObjsect.length == 0) {
            return;
        }
        for (Object o : releaseObjsect) {
            if (o instanceof BaseUseCase) {
                ((BaseUseCase) o).release();
            } else if (o instanceof BaseUseCaseGroup) {
                ((BaseUseCaseGroup) o).release();
            }
        }

        if (mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()) {
            return;
        }
        mCompositeSubscription.unsubscribe();
        mCompositeSubscription = null;
    }

    @Override
    public void execute(R req, C callback) {
        this.callback = callback;
        this.req = req;
    }

    @Override
    public void execute(R req) {
        this.req = req;
    }

    @Override
    public void setRspCallback(C callback) {
        this.callback = callback;
    }
}
