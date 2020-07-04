package openklaster.mongo.config;

import openklaster.common.config.NestedConfigAccessor;
import openklaster.mongo.parser.EntityParser;
import openklaster.mongo.service.EntityHandler;
import openklaster.mongo.service.MongoPersistenceService;

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
