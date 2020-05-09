package com.qtimes.pavilion.base.rx;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.qtimes.pavilion.base.rx.lifecycle.LayoutEvent;
import com.qtimes.pavilion.base.rx.lifecycle.LayoutLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import rx.subjects.BehaviorSubject;

/**
 * Created by gufei on 2016/9/6 0006.
 */

public abstract class RxFrameLayout extends FrameLayout implements LayoutLifecycleProvider {
    private final BehaviorSubject<LayoutEvent> lifecycleSubject = BehaviorSubject.create();

    public RxFrameLayout(Context context) {
        super(context);
    }

    public RxFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
