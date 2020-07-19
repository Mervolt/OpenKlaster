package com.openklaster.mongo.service;

import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.mongo.app.MongoVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Before;

public abstract class MongoServiceTest {

    protected Vertx vertx;
    protected EventBus eventBus;
    protected long testTimeoutMillis = 12000L;
    protected static String removedEntitiesKey = "removed_count";

    @Before
    public void setup(TestContext context) {
        ConfigFilesManager configFilesManager = new ConfigFilesManager();
        Async async = context.async();
        this.vertx = Vertx.vertx();
        MongoVerticle myMongoVerticle = new MongoVerticle(vertx, configFilesManager.getConfig(vertx));
        this.vertx.deployVerticle(myMongoVerticle, result -> async.complete());
        this.eventBus = vertx.eventBus();
        async.awaitSuccess();
    }

    JsonObject prepareRemovedEntities(long removedEntitiesAmount) {
        if (removedEntitiesAmount == 0) {
            return new JsonObject();
        } else {
            return new JsonObject().put(removedEntitiesKey, removedEntitiesAmount);
        }
    }
}