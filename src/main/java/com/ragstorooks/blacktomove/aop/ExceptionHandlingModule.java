package com.ragstorooks.blacktomove.aop;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class ExceptionHandlingModule extends AbstractModule {
    @Override
    protected void configure() {
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(ExceptionHandled.class), new ExceptionHandler());
    }
}
