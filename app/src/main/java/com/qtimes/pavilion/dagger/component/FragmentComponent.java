package com.qtimes.pavilion.dagger.component;
import com.qtimes.domain.dagger.scope.FragmentScope;

import com.qtimes.pavilion.dagger.base.BaseComponent;
import com.qtimes.pavilion.dagger.modules.FragmentModule;
import dagger.Subcomponent;


/**
 * Created by lt
 */
@FragmentScope
@Subcomponent(modules = {FragmentModule.class})
public interface FragmentComponent extends BaseComponent {
    CommonFragmentComponent provideCommonComponent();

}