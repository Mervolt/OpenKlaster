package config;

import model.User;
import parser.UserParser;
import service.MongoPersistenceService;
import service.UserHandler;

public class UserConfig  extends EntityConfig<User>{

    public UserConfig(MongoPersistenceService service, UserParser parser) {
        super(parser, new UserHandler(parser,service));
        mongoPersistenceService = service;
        collectionName= "userz";
        route="/users/";
    }
}
