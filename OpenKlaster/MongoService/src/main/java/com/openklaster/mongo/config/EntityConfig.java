package com.openklaster.mongo.config;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.mongo.parser.EntityParser;
import com.openklaster.mongo.service.EntityHandler;
import com.openklaster.mongo.service.MongoPersistenceService;

public abstract class EntityConfig {

    protected EntityParser parser;
    protected EntityHandler handler;
    protected MongoPersistenceService mongoPersistenceService;
    protected NestedConfigAccessor config;


    public EntityConfig(EntityParser parser, EntityHandler handler, NestedConfigAccessor config){
        this.handler=handler;
        this.parser=parser;
        this.config=config;
    }

    public  EntityParser getParser(){
        return this.parser;
    }
    public EntityHandler getHandler(){return this.handler;}
    public String getCollectionName(){return this.config.getString("mongo.collectionName");}
    public String getBusAddress(){return this.config.getString("bus.address");}

}
