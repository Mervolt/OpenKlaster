package com.openklaster.api;

import com.openklaster.api.app.OpenKlasterAPIVerticle;
import com.openklaster.api.properties.EndpointRouteProperties;
import com.openklaster.api.properties.EventBusAddressProperties;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.openklaster.api.app.OpenKlasterAPIVerticle.buildEndpoint;
import static com.openklaster.api.utils.PrepareData.getInstallationJsonObject;
import static com.openklaster.api.utils.PrepareData.getInstallationJsonObjectWithId;

public class TestBase {
    protected static final String ADDRESS = "localhost";
    protected static final int VERSION1 = 1;
    protected int port;
    protected ConfigRetriever configRetriever;
    protected NestedConfigAccessor configAccessor;
    protected Vertx vertx;
    protected OpenKlasterAPIVerticle verticle;
    protected EventBus eventBus;

    @Before
    public void setUp(TestContext context) {
        Async async = context.async();
        ConfigFilesManager configFilesManager = new ConfigFilesManager();
        vertx = Vertx.vertx();
        configRetriever =  configFilesManager.getConfig(vertx);
        verticle = new OpenKlasterAPIVerticle(vertx, configFilesManager.getConfig(vertx));
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
