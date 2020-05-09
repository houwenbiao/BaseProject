package com.qtimes.pavilion.dagger.modules;

import android.app.Application;
import android.content.Context;

import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.domain.dagger.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lt
 */
@Module
public class ApplicationModule {

    private Application appConfig;

    public ApplicationModule(Application app) {
        appConfig = app;
    }

    @Provides
    @ContextLevel(ContextLevel.APPLICATION)
    @ApplicationScope
    public Context provideContext() {
        return appConfig.getApplicationContext();
    }
}
