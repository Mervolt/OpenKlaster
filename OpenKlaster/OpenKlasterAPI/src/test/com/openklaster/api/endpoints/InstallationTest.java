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

import static com.openklaster.api.app.ApiVerticle.buildEndpoint;
import static com.openklaster.api.utils.PrepareData.getInstallationJsonObject;
import static com.openklaster.api.utils.PrepareData.getInstallationJsonObjectWithId;


@RunWith(VertxUnitRunner.class)
public class InstallationTest extends TestBase {

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
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("apiToken", "token");

        HashMap<String, Object> bodyParams = getInstallationJsonObject();

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).post(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }

    @Test
    public void testUpdateInstallation(TestContext context) {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("apiToken", "token");

        HashMap<String, Object> bodyParams = getInstallationJsonObjectWithId();

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).put(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
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
}
