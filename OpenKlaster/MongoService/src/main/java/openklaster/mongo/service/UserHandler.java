package openklaster.mongo.service;

import openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.logging.LoggerFactory;
import openklaster.mongo.parser.UserParser;

public class UserHandler extends EntityHandler {

    public UserHandler(UserParser parser, MongoPersistenceService service, NestedConfigAccessor config){
        super(parser,service, config);
        logger = LoggerFactory.getLogger(UserHandler.class);
    }

}
