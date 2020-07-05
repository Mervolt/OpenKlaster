package com.openklaster.mongo.service;

import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.logging.LoggerFactory;
import com.openklaster.mongo.parser.UserParser;

public class UserHandler extends EntityHandler {

    public UserHandler(UserParser parser, MongoPersistenceService service, NestedConfigAccessor config){
        super(parser,service, config);
        logger = LoggerFactory.getLogger(UserHandler.class);
    }

}
