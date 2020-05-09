package com.qtimes.wonly.dagger.base;

import com.qtimes.wonly.dagger.component.ActivityComponent;
import com.qtimes.wonly.dagger.component.ApplicationComponent;
import com.qtimes.wonly.dagger.component.FragmentComponent;
import com.qtimes.wonly.dagger.modules.ActivityModule;
import com.qtimes.wonly.dagger.modules.FragmentModule;
import com.qtimes.wonly.app.App;

/**获取相应的Dagger提供者
 * Created by liutao on 2017/2/7.
 */

public class DaggerDelegate {

    public static ApplicationComponent provideAppComponent(){
        return App.getInstance().getApplicationComponent();
    }

    public static ActivityComponent provideActivityComponent(ActivityModule activityModule){
        return provideAppComponent().provideActivityComponent(activityModule);
    }

    public static FragmentComponent provideFragmentComponent(FragmentModule fragmentModule){
        return provideAppComponent().provideFragmentComponent(fragmentModule);
    }




}
