package com.qtimes.pavilion.base.mvp;

import android.content.Context;

import com.qtimes.domain.base.BaseUseCase;
import com.qtimes.domain.base.BaseUseCaseGroup;
import com.qtimes.pavilion.dagger.PresenterProvide;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by PLU on 2016/6/8.
 */
public class BasePresenter<V extends MvpView> extends MvpBasePresenter<V> {
    //子类不直接使用provider，通过getProvide来使用
    protected Context context;
    protected CompositeSubscription mCompositeSubscription;
    private Object[] releaseObjects;//需要释放的对象
    private Object provider;
    public BasePresenter(PresenterProvide presenterProvide, Object... objects) {
        this.releaseObjects = objects;
        if (isViewAttached()) {
            return;
        }
        this.provider=presenterProvide.getProvider();
        this.context= presenterProvide.getContext();
        attachView((V) presenterProvide.getProvider());
    }


    public Object getProvide() {
        return provider;
    }

    protected void unsubscribeSubscription() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public Context getContext() {
        return context;
    }

    protected void releaseUseCase() {
        if (releaseObjects == null || releaseObjects.length == 0) {
            return;
        }
        for (Object o : releaseObjects) {
            if (o == null) {
                continue;
            }
            if (o instanceof BaseUseCase) {
                ((BaseUseCase) o).release();
            } else if (o instanceof BaseUseCaseGroup) {
                ((BaseUseCaseGroup) o).release();
            }
        }
    }

    @Override
    public void detachView() {
        release();
        super.detachView();
    }


    public void release() {
        releaseUseCase();
        unsubscribeSubscription();
    }
}
