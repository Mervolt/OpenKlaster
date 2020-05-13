package service;

import io.vertx.core.logging.LoggerFactory;
import model.Load;
import parser.EntityParser;

public class LoadHandler extends EntityHandler<Load> {

    public LoadHandler(EntityParser<Load> parser, MongoPersistenceService service) {
        super(parser, service);
        logger = LoggerFactory.getLogger(LoadHandler.class);
        collectionName = "loads";
    }
}
