package com.ragstorooks.blacktomove.database;

import com.google.inject.AbstractModule;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameDAO.class).to(DatomicGameDAO.class);
    }
}
