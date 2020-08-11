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

// Todo
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
        params.put("apiToken", "token");
        params.put("username", "test");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), params).send(handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testGenerateToken(TestContext context) {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("apiToken", "token");

        HashMap<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("username", "test");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).post(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }

    @Test
    public void testDeleteToken(TestContext context) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("apiToken", "token");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        addQueryParams(WebClient.create(vertx).delete(port, ADDRESS, route), params).send(handler(context));
        receiveMessageFromEventhandler(context, address, params);
    }

    @Test
    public void testGetInstallations(TestContext context) {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("apiToken", "token");
        queryParams.put("installationId", "installation:1");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(queryParams), handler(context));
        receiveMessageFromEventhandler(context, address, queryParams);
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
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("apiToken", "token");
        queryParams.put("installationId", "installation:1");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey);

        addQueryParams(WebClient.create(vertx).delete(port, ADDRESS, route), queryParams).send(handler(context));
        receiveMessageFromEventhandler(context, address, queryParams);
    }

    @Test
    public void testGetPowerConsumption(TestContext context) {
        // Todo
    }

    @Test
    public void testAddPowerConsumption(TestContext context) {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("apiToken", "token");

        HashMap<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("installationId", "installation:1");
        bodyParams.put("timestamp", "2020-07-18 20:10:08");
        bodyParams.put("value", 1.1);

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).post(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler2(context, address, bodyParams);
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

      private void receiveMessageFromEventhandler(TestContext context, String address, HashMap<String, Object> messageBody) {
        Async async = context.async();
        MessageConsumer<JsonObject> consumer = eventBus.consumer(address);
        consumer.handler(message -> {
            for (String entry : messageBody.keySet()) {
                if (entry != "apiToken" && entry != "sessionToken") {
                    context.assertEquals(message.body().getString(entry), messageBody.get(entry).toString());
                }
            }
            async.complete();
        });
    }

    private void receiveMessageFromEventhandler2(TestContext context, String address, HashMap<String, Object> messageBody) {
        Async async = context.async();
        MessageConsumer<JsonObject> consumer = eventBus.consumer(address);
        consumer.handler(message -> {
            for (String entry : messageBody.keySet()) {
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
            async.complete();
        });
    }
}
