package com.qtimes.pavilion.dagger.modules;

import android.content.Context;
import androidx.fragment.app.Fragment;

import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.domain.dagger.scope.FragmentScope;
import com.trello.rxlifecycle.FragmentLifecycleProvider;

import com.qtimes.pavilion.dagger.PresenterProvide;
import dagger.Module;
import dagger.Provides;

/**
 * Created by lt
 */
@Module
public class FragmentModule {

    private FragmentLifecycleProvider provider;

    public FragmentModule(FragmentLifecycleProvider provider) {
        this.provider = provider;
    }

    @Provides
    @FragmentScope
    @ContextLevel(ContextLevel.ACTIVITY)
    Context proviedContext() {
        return ((Fragment) provider).getActivity();
    }

    @Provides
    @FragmentScope
    FragmentLifecycleProvider providerFragmentProvider() {
        return provider;
    }

    @Provides
    @FragmentScope
    PresenterProvide providePresentProvide() {
        return new PresenterProvide(((Fragment)  provider).getActivity(),provider);
    }
}
