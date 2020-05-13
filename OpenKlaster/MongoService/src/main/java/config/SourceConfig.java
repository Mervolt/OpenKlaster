package config;

import model.Source;
import parser.SourceParsers;
import service.MongoPersistenceService;
import service.SourceHandler;

public class SourceConfig extends EntityConfig<Source> {
    public SourceConfig(MongoPersistenceService service, SourceParsers parser) {
        super(parser, new SourceHandler(parser,service));
        mongoPersistenceService = service;
        collectionName= "sources";
        route="/sources/";
    }
}
