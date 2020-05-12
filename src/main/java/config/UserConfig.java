package config;

import parser.UserParser;
import service.MongoPersistenceService;
import service.UserHandler;

public class UserConfig {

    MongoPersistenceService mongoPersistenceService;
    public UserConfig(MongoPersistenceService service) {
        this.mongoPersistenceService = service;
    }
}
