package com.qtimes.pavilion.base.rx.lifecycle;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * author: liutao
 * date: 2016/8/3.
 */
public interface LayoutLifecycleProvider {
    @NonNull
    @CheckResult
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull LayoutEvent event);
}
