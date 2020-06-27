package config;

import model.Installation;
import parser.InstallationParser;
import service.InstallationHandler;
import service.MongoPersistenceService;

public class InstallationConfig extends EntityConfig{

    public InstallationConfig(MongoPersistenceService service, InstallationParser parser, NestedConfigAccessor config) {
        super(parser, new InstallationHandler(parser,service, config), config);
        mongoPersistenceService = service;
    }
}
