package com.qtimes.pavilion.dagger.modules;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.domain.dagger.scope.ActivityScope;
import com.trello.rxlifecycle.ActivityLifecycleProvider;

import com.qtimes.pavilion.dagger.PresenterProvide;
import dagger.Module;
import dagger.Provides;


/**
 * Created by lt
 */
@Module
public class  ActivityModule {

    private ActivityLifecycleProvider provider;

    public ActivityModule(ActivityLifecycleProvider provider) {
        this.provider = provider;
    }

    @Provides
    @ActivityScope
    @ContextLevel(ContextLevel.ACTIVITY)
    Context proviedContext() {
        return (FragmentActivity) provider;
    }

    @Provides
    @ActivityScope
    ActivityLifecycleProvider providerActivityProvider() {
        return provider;
    }

    @Provides
    @ActivityScope
    PresenterProvide providePresentProvide() {
        return new PresenterProvide((FragmentActivity)  provider,provider);
    }


}
