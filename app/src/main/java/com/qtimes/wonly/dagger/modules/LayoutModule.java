package com.qtimes.wonly.dagger.modules;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qtimes.wonly.base.rx.lifecycle.LayoutLifecycleProvider;
import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.domain.dagger.scope.LayoutScope;

import com.qtimes.wonly.dagger.PresenterProvide;
import dagger.Module;
import dagger.Provides;


/**
 * Created by lt
 */
@Module
public class LayoutModule {

    private LayoutLifecycleProvider provider;
    private Context context;

    public LayoutModule(Context context) {
        this(null, context);
    }

    public LayoutModule(LayoutLifecycleProvider provider, Context context) {
        this.provider = provider;
        this.context = context;
    }

    @Provides
    @LayoutScope
    @ContextLevel(ContextLevel.ACTIVITY)
    Context proviedContext() {
        return context;
    }

    @Provides
    @LayoutScope
    LayoutLifecycleProvider providerActivityProvider() {
        return provider;
    }

    @Provides
    @LayoutScope
    PresenterProvide getPresenterProvide(AccountCache accountCache) {
        return new PresenterProvide( context,provider);
    }

    //提供一些需要context的元素
    @Provides
    @LayoutScope
    LinearLayoutManager proviedLinearLayoutManager(@ContextLevel(ContextLevel.ACTIVITY) Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    @LayoutScope
    public FragmentManager provideFragmentManager() {
        return ((FragmentActivity) context).getSupportFragmentManager();
    }
}
