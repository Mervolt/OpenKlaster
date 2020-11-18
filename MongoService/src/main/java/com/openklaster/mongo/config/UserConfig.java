package com.openklaster.mongo.config;

import com.openklaster.mongo.parser.UserParser;
import com.openklaster.mongo.service.MongoPersistenceService;
import com.openklaster.mongo.service.UserHandler;

public class UserConfig extends EntityConfig {

    public UserConfig(MongoPersistenceService service, UserParser parser, String collectionName,
                      String busAddress) {
        super(parser, new UserHandler(parser, service, collectionName), collectionName, busAddress);
        mongoPersistenceService = service;

    }
}
