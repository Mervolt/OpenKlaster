package com.openklaster.mongo.service;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.Installation;
import com.openklaster.mongo.parser.EntityParser;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

public class InstallationHandler extends EntityHandler {

    static final String installationCounter = "installationId";
    private static final String counterValueKey = "seq";

    public InstallationHandler(EntityParser<Installation> parser,
                               MongoPersistenceService service,
                               NestedConfigAccessor config) {
        super(parser, service, config);
        logger = LoggerFactory.getLogger(InstallationHandler.class);
    }

    @Override
    public void add(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        if (!jsonObject.containsKey(ID_KEY) || jsonObject.getValue(ID_KEY) == null) {
            persistenceService.getCounter(installationCounter, handler -> {
                if (handler.succeeded()) {
                    int seq = handler.result().getInteger(counterValueKey);

                    updateCounter(busMessage, seq);
                }else{
                    //if there is no counter document then add with starting value
                    updateCounter(busMessage, 0);
                }
            });
        }else{
            super.add(busMessage);
        }

    }

    private void updateCounter(Message<JsonObject> busMessage, int counterValue){
        String id = "installation:" + counterValue;
        busMessage.body().put(ID_KEY, id);
        persistenceService.updateCounter(installationCounter,counterValue +1 ,handler ->{
            if(handler.succeeded()){
                super.add(busMessage);
            }else{
                BusMessageReplyUtils.replyWithError(busMessage, HttpResponseStatus.INTERNAL_SERVER_ERROR,"");
            }
        });
    }
}
