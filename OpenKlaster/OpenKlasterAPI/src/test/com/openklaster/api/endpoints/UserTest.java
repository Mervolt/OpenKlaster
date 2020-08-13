package com.openklaster.api.endpoints;

import com.openklaster.api.TestBase;
import com.openklaster.api.properties.EndpointRouteProperties;
import com.openklaster.api.properties.EventBusAddressProperties;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.openklaster.api.app.OpenKlasterAPIVerticle.buildEndpoint;


@RunWith(VertxUnitRunner.class)
public class UserTest extends TestBase {

    @Test
    public void testLogin(TestContext context) {
        HashMap<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("username", "test");
        bodyParams.put("password", "test");
        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.loginEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);
        WebClient.create(vertx).post(port, ADDRESS, route).sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }

    @Test
    public void testRegister(TestContext context) {
        HashMap<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("username", "test");
        bodyParams.put("password", "test");
        bodyParams.put("email", "test@test");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        WebClient.create(vertx).post(port, ADDRESS, route).sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }

    @Test
    public void testUpdateUser(TestContext context) {
        HashMap<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("username", "test");
        bodyParams.put("password", "test");
        bodyParams.put("newPassword", "newTest");
        bodyParams.put("email", "test@test");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        WebClient.create(vertx).put(port, ADDRESS, route).sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }

    @Test
    public void testGetUsers(TestContext context) {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("apiToken", "token");
        queryParams.put("username", "test");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.userCoreAddressKey);

        addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), queryParams).send(handler(context));
        receiveMessageFromEventhandler(context, address, queryParams);
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
}
