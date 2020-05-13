package config;

import model.Load;
import parser.LoadParser;
import service.LoadHandler;
import service.MongoPersistenceService;

public class LoadConfig extends EntityConfig<Load> {
    public LoadConfig(MongoPersistenceService service, LoadParser parser) {
        super(parser, new LoadHandler(parser,service));
        mongoPersistenceService = service;
        collectionName= "loads";
        route="/loads/";
    }
}
