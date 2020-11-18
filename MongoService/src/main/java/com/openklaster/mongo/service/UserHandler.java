package com.openklaster.mongo.service;

import com.openklaster.mongo.parser.UserParser;
import io.vertx.core.logging.LoggerFactory;

public class UserHandler extends EntityHandler {

    public UserHandler(UserParser parser, MongoPersistenceService service, String collectionName) {
        super(parser, service, collectionName);
        logger = LoggerFactory.getLogger(UserHandler.class);
    }
}
