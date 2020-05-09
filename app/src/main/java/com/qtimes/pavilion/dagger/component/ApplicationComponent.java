package com.qtimes.pavilion.dagger.component;

import com.qtimes.pavilion.app.App;
import com.qtimes.data.module.ApiModule;
import com.qtimes.domain.dagger.scope.ApplicationScope;
import com.qtimes.pavilion.dagger.modules.LayoutModule;

import com.qtimes.pavilion.dagger.base.BaseComponent;
import com.qtimes.pavilion.dagger.modules.ActivityModule;
import com.qtimes.pavilion.dagger.modules.ApplicationModule;
import com.qtimes.pavilion.dagger.modules.FragmentModule;

import dagger.Component;

/**
 * 提供各种component
 * Created by lt
 */
@ApplicationScope
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent extends BaseComponent
{
    ActivityComponent provideActivityComponent(ActivityModule activityModule);

    FragmentComponent provideFragmentComponent(FragmentModule fragmentModule);

    LayoutComponent provideLayoutComponent(LayoutModule layoutModule);

    void inject(App app);
}
