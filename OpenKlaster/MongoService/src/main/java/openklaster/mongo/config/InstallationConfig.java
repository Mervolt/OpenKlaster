package openklaster.mongo.config;

import openklaster.common.config.NestedConfigAccessor;
import openklaster.mongo.parser.InstallationParser;
import openklaster.mongo.service.InstallationHandler;
import openklaster.mongo.service.MongoPersistenceService;

public class InstallationConfig extends EntityConfig{

    public InstallationConfig(MongoPersistenceService service, InstallationParser parser, NestedConfigAccessor config) {
        super(parser, new InstallationHandler(parser,service, config), config);
        mongoPersistenceService = service;
    }
}
