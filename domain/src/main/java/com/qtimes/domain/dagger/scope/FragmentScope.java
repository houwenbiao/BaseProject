package com.qtimes.domain.dagger.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by lt
 */
@Scope
@Retention(RUNTIME)
public @interface FragmentScope {

}