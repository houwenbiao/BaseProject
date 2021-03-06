package com.qtimes.pavilion.dagger.component;
import com.qtimes.domain.dagger.scope.LayoutScope;

import com.qtimes.pavilion.dagger.modules.LayoutModule;
import dagger.Subcomponent;

/**
 * author: liutao
 * date: 2016/8/3.
 */
@LayoutScope
@Subcomponent(modules = {LayoutModule.class})
public interface LayoutComponent {
    CommonLayoutComponent provideCommonComponent();
}
