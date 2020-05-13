package config;

import model.Source;
import parser.SourceParser;
import service.MongoPersistenceService;
import service.SourceHandler;

public class SourceConfig extends EntityConfig<Source> {
    public SourceConfig(MongoPersistenceService service, SourceParser parser) {
        super(parser, new SourceHandler(parser,service));
        mongoPersistenceService = service;
        collectionName= "sources";
        route="/sources/";
    }
}
