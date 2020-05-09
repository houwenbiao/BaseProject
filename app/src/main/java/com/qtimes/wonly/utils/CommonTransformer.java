package com.qtimes.wonly.utils;


import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 一个公共的Transformer，用来处理Observe的一些重复方法
 * Created by PLU on 2016/6/7.
 */
public class CommonTransformer<T> implements Observable.Transformer<T, T> {
    private Scheduler observeScheduler;
    private Scheduler subscribeScheduler;
    private Object provider;
    private Object event;
    private CommonTransformer() {//不允许直接new
    }

    public static class Builder<T> {
        private Scheduler observeScheduler = AndroidSchedulers.mainThread();//观察者线程调度器,默认主线程
        private Scheduler subscribeScheduler = Schedulers.io();//被观察者  默认io线程
        private Object provider;
        private Object event;
        public Builder() {
        }

        public Builder(Object provide) {
            this.provider=provide;
        }
        public Builder<T> observeOn(Scheduler observeScheduler) {
            this.observeScheduler = observeScheduler;
            return this;
        }
        public Builder<T> subscribeOn(Scheduler subscribeScheduler) {
            this.subscribeScheduler = subscribeScheduler;
            return this;
        }
        public Builder<T> buildEvent(Object event){
            this.event=event;
            return  this;
        }

        public CommonTransformer<T> build() {
            CommonTransformer<T> commonTransformer = new CommonTransformer<>();
            commonTransformer.subscribeScheduler = subscribeScheduler;
            commonTransformer.observeScheduler = observeScheduler;
            commonTransformer.event=event;
            commonTransformer.provider=provider;
            return commonTransformer;
        }
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        if (provider instanceof ActivityLifecycleProvider) {
            if( !(event instanceof ActivityEvent)){
                event= ActivityEvent.DESTROY;
            }
            return observable.compose(((ActivityLifecycleProvider)provider).<T>bindUntilEvent((ActivityEvent)event)).subscribeOn(subscribeScheduler).observeOn(observeScheduler);
        }else if(provider instanceof FragmentLifecycleProvider){
            if( !(event instanceof FragmentEvent)){
                event= FragmentEvent.DESTROY_VIEW;
            }
            return observable.compose(((FragmentLifecycleProvider)provider).<T>bindUntilEvent((FragmentEvent)event)).subscribeOn(subscribeScheduler).observeOn(observeScheduler);
        }
        return observable.subscribeOn(subscribeScheduler).observeOn(observeScheduler);
    }

}
