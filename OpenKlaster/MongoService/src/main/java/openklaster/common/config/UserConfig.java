package openklaster.common.config;

import parser.UserParser;
import service.MongoPersistenceService;
import service.UserHandler;

public class UserConfig  extends EntityConfig{

    public UserConfig(MongoPersistenceService service, UserParser parser, NestedConfigAccessor config) {
        super(parser, new UserHandler(parser,service, config), config);
        mongoPersistenceService = service;

    }
}
