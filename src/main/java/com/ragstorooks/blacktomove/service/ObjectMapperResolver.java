package com.ragstorooks.blacktomove.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;

import javax.ws.rs.ext.ContextResolver;

@Singleton
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {
    private ObjectMapper defaultObjectMapper = new ObjectMapper();

    ObjectMapperResolver() {
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return defaultObjectMapper;
    }
}
