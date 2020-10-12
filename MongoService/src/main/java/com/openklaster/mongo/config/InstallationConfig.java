package com.openklaster.mongo.config;

import com.openklaster.mongo.parser.InstallationParser;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.mongo.service.InstallationHandler;
import com.openklaster.mongo.service.MongoPersistenceService;

public class InstallationConfig extends EntityConfig{

    public InstallationConfig(MongoPersistenceService service, InstallationParser parser, NestedConfigAccessor config) {
        super(parser, new InstallationHandler(parser,service, config), config);
        mongoPersistenceService = service;
    }
}