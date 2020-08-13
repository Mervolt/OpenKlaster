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
import static com.openklaster.api.utils.PrepareData.getApiToken;
import static com.openklaster.api.utils.PrepareData.getMeasurementJsonObject;


@RunWith(VertxUnitRunner.class)
public class MeasurementsTest extends TestBase {

    @Test
    public void testGetPowerConsumption(TestContext context) {
        HashMap<String, Object> queryParams = getApiToken();
        queryParams.put("installationId", "installation:1");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(queryParams), handler(context));
        receiveMessageFromEventhandler(context, address, queryParams);
    }

    @Test
    public void testAddPowerConsumption(TestContext context) {
        HashMap<String, Object> queryParams = getApiToken();
        HashMap<String, Object> bodyParams = getMeasurementJsonObject();

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).post(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }

    @Test
    public void testGetPowerProduction(TestContext context) {
        HashMap<String, Object> queryParams = getApiToken();
        queryParams.put("installationId", "installation:1");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.powerproductionCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(queryParams), handler(context));
        receiveMessageFromEventhandler(context, address, queryParams);
    }

    @Test
    public void testAddPowerProduction(TestContext context) {
        HashMap<String, Object> queryParams = getApiToken();
        HashMap<String, Object> bodyParams = getMeasurementJsonObject();

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.powerproductionCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).post(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }

    @Test
    public void testGetEnergyConsumed(TestContext context) {
        HashMap<String, Object> queryParams = getApiToken();
        queryParams.put("installationId", "installation:1");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.energyconsumedCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(queryParams), handler(context));
        receiveMessageFromEventhandler(context, address, queryParams);
    }

    @Test
    public void testAddEnergyConsumed(TestContext context) {
        HashMap<String, Object> queryParams = getApiToken();
        HashMap<String, Object> bodyParams = getMeasurementJsonObject();

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.energyconsumedCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).post(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }

    @Test
    public void testGetEnergyProduced(TestContext context) {
        HashMap<String, Object> queryParams = getApiToken();
        queryParams.put("installationId", "installation:1");

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.energyproducedCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).get(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(queryParams), handler(context));
        receiveMessageFromEventhandler(context, address, queryParams);
    }

    @Test
    public void testAddEnergyProduced(TestContext context) {
        HashMap<String, Object> queryParams = getApiToken();
        HashMap<String, Object> bodyParams = getMeasurementJsonObject();

        String route = buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint);
        String address = configAccessor.getString(EventBusAddressProperties.energyproducedCoreAddressKey);

        HttpRequest<Buffer> request = addQueryParams(WebClient.create(vertx).post(port, ADDRESS, route), queryParams);
        request.sendJsonObject(prepareJsonObject(bodyParams), handler(context));
        receiveMessageFromEventhandler(context, address, bodyParams);
    }
}
