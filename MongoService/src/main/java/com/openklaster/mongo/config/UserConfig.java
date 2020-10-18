package com.openklaster.mongo.config;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.mongo.parser.UserParser;
import com.openklaster.mongo.service.MongoPersistenceService;
import com.openklaster.mongo.service.UserHandler;

public class UserConfig extends EntityConfig {

    public UserConfig(MongoPersistenceService service, UserParser parser, NestedConfigAccessor config, String collectionName) {
        super(parser, new UserHandler(parser, service, collectionName), config);
        mongoPersistenceService = service;

    }
}
