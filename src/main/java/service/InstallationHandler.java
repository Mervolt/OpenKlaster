package service;

import io.vertx.core.logging.LoggerFactory;
import model.Installation;
import parser.EntityParser;

public class InstallationHandler  extends EntityHandler<Installation>{
    public InstallationHandler(EntityParser<Installation> parser, MongoPersistenceService service) {
        super(parser, service);
        collectionName = "installations";
        logger = LoggerFactory.getLogger(InstallationHandler.class);
    }
}
