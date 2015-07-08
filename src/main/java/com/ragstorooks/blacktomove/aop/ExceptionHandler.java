package com.ragstorooks.blacktomove.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;


public class ExceptionHandler implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable t){
            logger.error("Uncaught exception in service while invoking " + invocation.getMethod(), t);
            return Response.serverError().build();
        }
    }
}
