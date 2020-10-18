package com.openklaster.mongo.config;

import com.openklaster.mongo.parser.EntityParser;
import com.openklaster.mongo.service.EntityHandler;
import com.openklaster.mongo.service.MongoPersistenceService;

public abstract class EntityConfig {

    protected EntityParser parser;
    protected EntityHandler handler;
    protected MongoPersistenceService mongoPersistenceService;
    protected String collectionName;
    protected String busAddress;


    public EntityConfig(EntityParser parser, EntityHandler handler, String collectionName, String busAddress) {
        this.handler = handler;
        this.parser = parser;
        this.collectionName = collectionName;
        this.busAddress = busAddress;
    }

    public EntityParser getParser() {
        return this.parser;
    }

    public EntityHandler getHandler() {
        return this.handler;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public String getBusAddress() {
        return this.busAddress;
    }

}
