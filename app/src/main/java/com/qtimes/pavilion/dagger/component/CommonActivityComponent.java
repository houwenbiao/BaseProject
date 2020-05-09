package com.qtimes.pavilion.dagger.component;

import com.qtimes.pavilion.activity.MainActivity;
import com.qtimes.pavilion.base.activity.WebViewActivity;
import com.qtimes.pavilion.dagger.base.BaseComponent;
import com.qtimes.pavilion.tip.QuDialogActivity;

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
