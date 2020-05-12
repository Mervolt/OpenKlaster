package service;

import io.vertx.core.logging.LoggerFactory;
import model.User;
import parser.UserParser;

public class UserHandler extends EntityHandler<User> {

    public UserHandler(UserParser parser, MongoPersistenceService service){
        super(parser,service);
        collectionName = "users";
        logger = LoggerFactory.getLogger(UserHandler.class);
    }
}
