package config;

import parser.EntityParser;
import service.EntityHandler;
import service.MongoPersistenceService;

//TODO ZROBIC
public abstract class EntityConfig<T> {

    protected EntityParser<T> parser;
    protected EntityHandler<T> handler;
    protected String collectionName;
    protected String route;
    protected MongoPersistenceService mongoPersistenceService;


    public EntityConfig(EntityParser<T> parser, EntityHandler<T> handler){
        this.handler=handler;
        this.parser=parser;
    }

    public  EntityParser<T> getParser(){
        return this.parser;
    }
    public EntityHandler<T> getHandler(){return this.handler;}
    public String getCollectionName(){return this.collectionName;}
    public String getRoute(){return this.route;}

}
