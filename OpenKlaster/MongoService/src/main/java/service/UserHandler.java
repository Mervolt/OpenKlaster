package service;

import openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.logging.LoggerFactory;
import parser.UserParser;

public class UserHandler extends EntityHandler {

    public UserHandler(UserParser parser, MongoPersistenceService service, NestedConfigAccessor config){
        super(parser,service, config);
        logger = LoggerFactory.getLogger(UserHandler.class);
    }

}
