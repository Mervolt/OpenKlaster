package com.openklaster.mongo.config;

import com.openklaster.mongo.parser.InstallationParser;
import com.openklaster.mongo.service.InstallationHandler;
import com.openklaster.mongo.service.MongoPersistenceService;

public class InstallationConfig extends EntityConfig {

    public InstallationConfig(MongoPersistenceService service, InstallationParser parser, String collectionName,
                              String busAddress) {
        super(parser, new InstallationHandler(parser, service, collectionName), collectionName, busAddress);
        mongoPersistenceService = service;
    }
}
