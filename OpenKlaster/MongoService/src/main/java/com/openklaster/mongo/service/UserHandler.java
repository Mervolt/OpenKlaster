package com.openklaster.mongo.service;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import com.openklaster.mongo.parser.UserParser;

public class UserHandler extends EntityHandler {

    public UserHandler(UserParser parser, MongoPersistenceService service, NestedConfigAccessor config){
        super(parser,service, config);
        logger = LoggerFactory.getLogger(UserHandler.class);
    }

    @Override
    public void add(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        persistenceService.insertByQuery(jsonObject, this.getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("[%s]Entity added - %s",
                                getCollectionName(),jsonObject.getString("_id")));
                        BusMessageReplyUtils.replyWithBodyAndStatus(busMessage, jsonObject, HttpResponseStatus.OK);
                    } else {
                        logger.warn(String.format("[%s]Problem with adding entity - %s. %s",
                                getCollectionName(), jsonObject, handler.cause().getMessage()));
                        BusMessageReplyUtils.replyWithError(busMessage, HttpResponseStatus.BAD_REQUEST,
                                "Could not add document!");
                    }
                });
    }

}
