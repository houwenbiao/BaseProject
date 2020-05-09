package com.qtimes.pavilion.dagger.base;

import com.qtimes.pavilion.dagger.component.ActivityComponent;
import com.qtimes.pavilion.dagger.component.ApplicationComponent;
import com.qtimes.pavilion.dagger.component.FragmentComponent;
import com.qtimes.pavilion.dagger.modules.ActivityModule;
import com.qtimes.pavilion.dagger.modules.FragmentModule;
import com.qtimes.pavilion.app.App;

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
