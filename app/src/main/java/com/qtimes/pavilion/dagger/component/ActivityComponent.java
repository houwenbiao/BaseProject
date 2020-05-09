package com.qtimes.pavilion.dagger.component;
import com.qtimes.domain.dagger.scope.ActivityScope;

import com.qtimes.pavilion.dagger.base.BaseComponent;
import com.qtimes.pavilion.dagger.modules.ActivityModule;
import dagger.Subcomponent;

/**
 * Created by lt
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent extends BaseComponent {
    CommonActivityComponent provideCommonComponent();
}
