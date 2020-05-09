package com.qtimes.wonly.dagger.component;
import com.qtimes.domain.dagger.scope.ActivityScope;

import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.dagger.modules.ActivityModule;
import dagger.Subcomponent;

/**
 * Created by lt
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent extends BaseComponent {
    CommonActivityComponent provideCommonComponent();
}
