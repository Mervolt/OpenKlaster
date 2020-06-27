package service;

import config.NestedConfigAccessor;
import io.vertx.core.logging.LoggerFactory;
import model.Installation;
import parser.EntityParser;

public class InstallationHandler extends EntityHandler {
    public InstallationHandler(EntityParser<Installation> parser,
                               MongoPersistenceService service,
                               NestedConfigAccessor config) {
        super(parser, service, config);
        logger = LoggerFactory.getLogger(InstallationHandler.class);
    }
}
