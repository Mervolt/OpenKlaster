package com.openklaster.mongo.service;

import com.openklaster.mongo.model.Installation;
import com.openklaster.mongo.parser.EntityParser;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.logging.LoggerFactory;

public class InstallationHandler extends EntityHandler {
    public InstallationHandler(EntityParser<Installation> parser,
                               MongoPersistenceService service,
                               NestedConfigAccessor config) {
        super(parser, service, config);
        logger = LoggerFactory.getLogger(InstallationHandler.class);
    }
}
