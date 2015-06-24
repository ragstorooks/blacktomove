package com.ragstorooks.blacktomove.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;

public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {
    private ObjectMapper defaultObjectMapper = new ObjectMapper();

    public ObjectMapperResolver() {
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return defaultObjectMapper;
    }
}
