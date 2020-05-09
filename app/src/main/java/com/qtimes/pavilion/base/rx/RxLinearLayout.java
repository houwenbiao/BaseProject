package com.qtimes.pavilion.base.rx;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.qtimes.pavilion.base.rx.lifecycle.LayoutEvent;
import com.qtimes.pavilion.base.rx.lifecycle.LayoutLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import rx.subjects.BehaviorSubject;

/**
 * 实现了绑定Rx生命周期的LinearLayout
 * author: liutao
 * date: 2016/8/3.
 */
public class RxLinearLayout extends LinearLayout implements LayoutLifecycleProvider {
    private final BehaviorSubject<LayoutEvent> lifecycleSubject = BehaviorSubject.create();

    public RxLinearLayout(Context context) {
        super(context);
    }

    public RxLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull LayoutEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        lifecycleSubject.onNext(LayoutEvent.ONATTACHEDTOWINDOW);
    }

    @Override
    protected void onDetachedFromWindow() {
        lifecycleSubject.onNext(LayoutEvent.ONDETACHEDFROMWINDOW);
        super.onDetachedFromWindow();
    }
}
