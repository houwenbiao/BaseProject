package com.qtimes.wonly.dagger.component;
import com.qtimes.domain.dagger.scope.FragmentScope;

import com.qtimes.wonly.dagger.base.BaseComponent;
import com.qtimes.wonly.dagger.modules.FragmentModule;
import dagger.Subcomponent;


/**
 * Created by lt
 */
@FragmentScope
@Subcomponent(modules = {FragmentModule.class})
public interface FragmentComponent extends BaseComponent {
    CommonFragmentComponent provideCommonComponent();

}