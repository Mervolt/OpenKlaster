package openklaster.mongo.service;

import openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.logging.LoggerFactory;
import openklaster.mongo.model.Installation;
import openklaster.mongo.parser.EntityParser;

public class InstallationHandler extends EntityHandler {
    public InstallationHandler(EntityParser<Installation> parser,
                               MongoPersistenceService service,
                               NestedConfigAccessor config) {
        super(parser, service, config);
        logger = LoggerFactory.getLogger(InstallationHandler.class);
    }
}
