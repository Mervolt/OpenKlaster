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
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

@RunWith(VertxUnitRunner.class)
public class APITest {
    private static final String ADDRESS = "localhost";
    private int port;
    private ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;
    private Vertx vertx;
    private OpenKlasterAPIVerticle verticle;
    private EventBus eventBus;

    @Before
    public void setUp(TestContext context) {
        Async async1 = context.async();
        vertx = Vertx.vertx();
        verticle = new OpenKlasterAPIVerticle();

        configRetriever = new ConfigFilesManager().getConfig(vertx);
        configRetriever.getConfig(config ->{
            if(config.succeeded()){
                this.configAccessor = new NestedConfigAccessor(config.result());
                port = configAccessor.getInteger(EndpointRouteProperties.listeningPortKey);
                vertx.deployVerticle(verticle, result -> async1.complete());
            }
        });


        Async async2 = context.async();
        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, result ->{
            if(result.succeeded()) {
                vertx = result.result();
                eventBus = vertx.eventBus();
                async2.complete();
            }
        });
    }

    @After
    public void tearDown() {
        vertx.close();
    }

    @Test
    public void testLogin(TestContext context) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", "test");
        params.put("password", "test");
        String route = configAccessor.getString(EndpointRouteProperties.loginEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.loginCoreAddressKey);

        WebClient.create(vertx).post(port, ADDRESS, route).sendJsonObject(prepareJsonObject(params), handler(context));
        receiveMessageFromEventhandler(context,address, params);
    }


    public JsonObject prepareJsonObject(HashMap<String, Object> bodyParams) {
        JsonObject jsonObject = new JsonObject();
        for (String entry : bodyParams.keySet()) {
            jsonObject.put(entry, bodyParams.get(entry));
        }
        return jsonObject;
    }

    private Handler<AsyncResult<HttpResponse<Buffer>>> handler(TestContext context) {
        return ar -> {
            if (ar.succeeded()) {
                Async async1 = context.async();
                context.assertEquals(ar.result().statusCode(), 500);
                async1.complete();
            }
        };
    }

    public void receiveMessageFromEventhandler(TestContext context, String address, HashMap<String, Object> messageBody) {
        Async async2 = context.async();
        MessageConsumer<JsonObject> consumer2 = eventBus.consumer(address);
        consumer2.handler(message -> {
            for (String entry : messageBody.keySet()) {
                context.assertEquals(message.body().getString(entry), messageBody.get(entry));
            }
            async2.complete();
        });
    }
}
