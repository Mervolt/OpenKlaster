package openklaster.mongo.config;

import openklaster.common.config.NestedConfigAccessor;
import openklaster.mongo.parser.UserParser;
import openklaster.mongo.service.MongoPersistenceService;
import openklaster.mongo.service.UserHandler;

public class UserConfig  extends EntityConfig{

    public UserConfig(MongoPersistenceService service, UserParser parser, NestedConfigAccessor config) {
        super(parser, new UserHandler(parser,service, config), config);
        mongoPersistenceService = service;

    }
}
