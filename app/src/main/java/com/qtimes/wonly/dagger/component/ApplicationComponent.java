package com.qtimes.wonly.dagger.component;

import com.qtimes.wonly.app.App;
import com.qtimes.data.module.ApiModule;
import com.qtimes.domain.dagger.scope.ApplicationScope;
import com.qtimes.wonly.dagger.modules.LayoutModule;

import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.dagger.modules.ActivityModule;
import com.qtimes.wonly.dagger.modules.ApplicationModule;
import com.qtimes.wonly.dagger.modules.FragmentModule;

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
