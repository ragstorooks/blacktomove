package com.ragstorooks.blacktomove.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionHandlerTest {
    @Mock
    private MethodInvocation invocation;

    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Test
    public void testThatTheExceptionHandlerReturnsExpectedValueWithNoException() throws Throwable {
        // setup
        when(invocation.proceed()).thenReturn("all happy");

        // act
        Object result = exceptionHandler.invoke(invocation);

        // verify
        assertThat(result, equalTo("all happy"));
    }

    @Test
    public void testThatTheExceptionHandlerReturnsServerErrorOnException() throws Throwable {
        // setup
        when(invocation.proceed()).thenThrow(new RuntimeException());

        // act
        Object result = exceptionHandler.invoke(invocation);

        // verify
        assertTrue(result instanceof Response);
        assertThat(((Response) result).getStatus(), equalTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }
}