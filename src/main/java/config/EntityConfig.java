package config;

import parser.EntityParser;
import service.EntityHandler;

//TODO ZROBIC
public abstract class EntityConfig<T> {

    protected EntityParser<T> parser;
    protected EntityHandler<T> handler;
    protected String collectionName;
    protected String route;

    public EntityConfig(EntityParser<T> parser){
        this.parser=parser;
    }

    public  EntityParser<T> getParser(){
        return this.parser;
    }

}
