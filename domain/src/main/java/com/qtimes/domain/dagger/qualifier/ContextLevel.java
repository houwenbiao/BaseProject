package com.qtimes.domain.dagger.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by lt
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ContextLevel {
    String APPLICATION = "Application";
    String ACTIVITY = "Activity";
    String FRAGMENT = "Fragment";
    String value() default APPLICATION;
}