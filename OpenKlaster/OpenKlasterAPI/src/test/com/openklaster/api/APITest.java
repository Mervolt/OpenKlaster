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

// Todo tests need general refactor. Each subsequent test case increases the cluster, so not all tests are performed
//  because there is not enough space, in this iteration there was not enough time for it
@RunWith(VertxUnitRunner.class)
public class APITest {
    private static final String ADDRESS = "localhost";
    private static final int VERSION1 = 1;
    private int port;
    private ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;
    private Vertx vertx;
    private OpenKlasterAPIVerticle verticle;
    private EventBus eventBus;
    private ClusterManager clusterManager;

    @Before
    public void setUp(TestContext context) {
        Async async1 = context.async();
        vertx = Vertx.vertx();
        verticle = new OpenKlasterAPIVerticle();

        configRetriever = new ConfigFilesManager().getConfig(vertx);
        configRetriever.getConfig(config -> {
            if (config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());
                port = configAccessor.getInteger(EndpointRouteProperties.listeningPortKey);
                vertx.deployVerticle(verticle, result -> async1.complete());
            }
        });


        Async async2 = context.async();
        clusterManager = new HazelcastClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, result -> {
            if (result.succeeded()) {
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
        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.loginEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        WebClient.create(vertx).post(port, ADDRESS, route).sendJsonObject(prepareJsonObject(params), handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }


    @Test
    public void testRegister(TestContext context) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", "test");
        params.put("password", "test");
        params.put("email", "test@test");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        WebClient.create(vertx).post(port, ADDRESS, route).sendJsonObject(prepareJsonObject(params), handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testUpdateUser(TestContext context) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", "test");
        params.put("password", "test");
        params.put("newPassword", "newTest");
        params.put("email", "test@test");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        WebClient.create(vertx).put(port, ADDRESS, route).sendJsonObject(prepareJsonObject(params), handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testGetUsers(TestContext context) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", "test");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), params).send(handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testGenerateToken(TestContext context) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", "test");
        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        WebClient.create(vertx).post(port, ADDRESS, route).sendJsonObject(prepareJsonObject(params), handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testDeleteToken(TestContext context) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("tokenId", 1);

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        addQueryParams(WebClient.create(vertx).delete(port, ADDRESS, route), params).send(handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testGetInstallations(TestContext context) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("installationId", 1);

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey);

        addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), params).send(handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testAddInstallation(TestContext context) {
        // Todo
    }

    @Test
    public void testUpdateInstallation(TestContext context) {
        // Todo
    }

    @Test
    public void testDeleteInstallations(TestContext context) {
        // Todo problem here
        HashMap<String, Object> params = new HashMap<>();
        params.put("installationId", 1);

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey);

        addQueryParams(WebClient.create(vertx).delete(port, ADDRESS, route), params).send(handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testGetPowerConsumption(TestContext context) {
        // Todo
    }

    @Test
    public void testAddPowerConsumption(TestContext context) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("installationId", 1);
        params.put("timestamp", "2020-07-18T20:10:08.904Z");
        params.put("unit", "test");
        params.put("value", 1.1);
        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey);
        WebClient.create(vertx).post(port, ADDRESS, route).sendJsonObject(prepareJsonObject(params), handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testGetPowerProduction(TestContext context) {
        // Todo
    }

    @Test
    public void testAddPowerProduction(TestContext context) {
        // Todo
    }

    @Test
    public void testGetEnergyConsumed(TestContext context) {
        // Todo
    }

    @Test
    public void testAddEnergyConsumed(TestContext context) {
        // Todo
    }

    @Test
    public void testGetEnergyProduced(TestContext context) {
        // Todo
    }

    @Test
    public void testAddEnergyProduced(TestContext context) {
        // Todo
    }

    private JsonObject prepareJsonObject(HashMap<String, Object> bodyParams) {
        JsonObject jsonObject = new JsonObject();
        for (String entry : bodyParams.keySet()) {
            jsonObject.put(entry, bodyParams.get(entry));
        }
        return jsonObject;
    }

    private HttpRequest<Buffer> addQueryParams(HttpRequest<Buffer> request, HashMap<String, Object> queryParams) {
        for (String entry : queryParams.keySet()) {
            request.addQueryParam(entry, queryParams.get(entry).toString());
        }
        return request;
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

    // Todo the method should be split into 2, separate for query params and separate for body params so that you don't have to use trycatch
    private void receiveMessageFromEventhandler(TestContext context, String address, HashMap<String, Object> messageBody) {
        Async async2 = context.async();
        MessageConsumer<JsonObject> consumer = eventBus.consumer(address);
        consumer.handler(message -> {
            for (String entry : messageBody.keySet()) {
                try {
                    context.assertEquals(message.body().getString(entry), messageBody.get(entry).toString());
                } catch (ClassCastException e) {
                    if (messageBody.get(entry) instanceof Integer) {
                        context.assertEquals(message.body().getInteger(entry), messageBody.get(entry));
                    }
                }
            }
            async2.complete();
        });
    }
}
