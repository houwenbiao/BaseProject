package com.qtimes.wonly.dagger.component;

import com.qtimes.wonly.activity.MainActivity;
import com.qtimes.wonly.base.activity.WebViewActivity;
import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.tip.QuDialogActivity;

import dagger.Subcomponent;

/**
 * author: liutao
 * date: 2016/6/22.
 */
@Subcomponent
public interface CommonActivityComponent extends BaseComponent {
    void inject(WebViewActivity webViewActivity);

    void inject(MainActivity mainActivity);

    void inject(QuDialogActivity quDialogActivity);
}
