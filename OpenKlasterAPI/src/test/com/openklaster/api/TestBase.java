package com.openklaster.api;

import com.openklaster.api.app.ApiVerticle;
import com.openklaster.api.properties.EndpointRouteProperties;
import com.openklaster.common.config.ConfigFilesManager;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import org.junit.After;
import org.junit.Before;

import java.util.HashMap;

public class TestBase {
    protected static final String ADDRESS = "localhost";
    protected static final int VERSION1 = 1;
    protected int port;
    protected ConfigRetriever configRetriever;
    protected NestedConfigAccessor configAccessor;
    protected Vertx vertx;
    protected ApiVerticle verticle;
    protected EventBus eventBus;

    @Before
    public void setUp(TestContext context) {
        Async async = context.async();
        ConfigFilesManager configFilesManager = new ConfigFilesManager("config-dev");
        vertx = Vertx.vertx();
        configRetriever =  configFilesManager.getConfig(vertx);
        verticle = new ApiVerticle();
        vertx.deployVerticle(verticle, result -> {
            async.complete();
        });
        this.eventBus = vertx.eventBus();

        Async asyn2c = context.async();
        configRetriever.getConfig(config -> {
            if (config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());
                port = configAccessor.getInteger(EndpointRouteProperties.listeningPortKey);
                asyn2c.complete();
            } else {
                vertx.close();
                asyn2c.complete();
            }
        });

    }

    @After
    public void tearDown() {
        vertx.close();
    }


    protected JsonObject prepareJsonObject(HashMap<String, Object> bodyParams) {
        JsonObject jsonObject = new JsonObject();
        for (String entry : bodyParams.keySet()) {
            jsonObject.put(entry, bodyParams.get(entry));
        }
        return jsonObject;
    }

    protected HttpRequest<Buffer> addQueryParams(HttpRequest<Buffer> request, HashMap<String, Object> queryParams) {
        for (String entry : queryParams.keySet()) {
            request.addQueryParam(entry, queryParams.get(entry).toString());
        }
        return request;
    }

    protected Handler<AsyncResult<HttpResponse<Buffer>>> handler(TestContext context) {
        return ar -> {
            if (ar.succeeded()) {
                Async async1 = context.async();
                context.assertEquals(ar.result().statusCode(), 500);
                async1.complete();
            }
        };
    }

    protected void receiveMessageFromEventhandler(TestContext context, String address, HashMap<String, Object> messageBody) {
        Async async = context.async();
        MessageConsumer<JsonObject> consumer = eventBus.consumer(address);
        consumer.handler(message -> {
            for (String entry : messageBody.keySet()) {
                if (entry != "apiToken" && entry != "sessionToken") {
                    if (messageBody.get(entry) instanceof Double) {
                        context.assertEquals(message.body().getDouble(entry), messageBody.get(entry));
                    }
                    if (messageBody.get(entry) instanceof Integer) {
                        context.assertEquals(message.body().getInteger(entry), messageBody.get(entry));
                    }
                    if (messageBody.get(entry) instanceof String) {
                        context.assertEquals(message.body().getString(entry), messageBody.get(entry));
                    }
                }
            }
            async.complete();
        });
    }
}
