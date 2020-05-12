package service;

import io.vertx.core.logging.LoggerFactory;
import model.Source;
import parser.EntityParser;

public class SourceHandler extends  EntityHandler<Source> {
    public SourceHandler(EntityParser<Source> parser, MongoPersistenceService service) {
        super(parser, service);
        logger = LoggerFactory.getLogger(SourceHandler.class);
        collectionName="sources";
    }
}
