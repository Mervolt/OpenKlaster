package com.openklaster.mongo.service;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import com.openklaster.mongo.parser.UserParser;

public class UserHandler extends EntityHandler {

    public UserHandler(UserParser parser, MongoPersistenceService service, NestedConfigAccessor config,){
        super(parser,service, config);
        logger = LoggerFactory.getLogger(UserHandler.class);
    }

    @Override
    public void add(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();

        persistenceService.insertByQuery(jsonObject, this.getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity added - %s", jsonObject.getString("_id")));
                        BusMessageReplyUtils.replyWithBodyAndStatus(busMessage, jsonObject, HttpResponseStatus.OK);
                    } else {
                        handleFailedQuery(jsonObject.getString("_id"),busMessage,handler.cause(),
                                "Problem with adding entity.");
                    }
                });
    }

    private JsonObject hashUserPassword(JsonObject jsonObject){
        User user = jsonObject.mapTo(User.class);
        return null;//TODO
    }

}
