package config;

import model.Installation;
import parser.InstallationParser;
import service.InstallationHandler;
import service.MongoPersistenceService;

public class InstallationConfig extends EntityConfig<Installation>{

    public InstallationConfig(MongoPersistenceService service, InstallationParser parser) {
        super(parser, new InstallationHandler(parser,service));
        mongoPersistenceService = service;
        collectionName= "installations";
        route="/installations/";
    }
}
