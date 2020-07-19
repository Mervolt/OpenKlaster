package com.openklaster.mongo.service;

import com.openklaster.common.model.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.openklaster.common.tests.bus.ResponsesAssertion.assertBusResult;
import static com.openklaster.common.tests.model.InstallationTestUtil.*;

@RunWith(VertxUnitRunner.class)
public class InstallationServiceTest extends MongoServiceTest {

    private static final String installationAddress = "mongo.installations";
    private Installation testInstallation;
    private Installation noIdInstallation;
    private Inverter testInverter;
    private Load testLoad;
    private Source testSource;

    @Before
    public void setupInstallation() {
        this.testInstallation = prepareInstallation("test");
        this.noIdInstallation = prepareInstallation();
        this.testInverter = testInstallation.getInverter();
        this.testLoad = testInstallation.getLoad();
        this.testSource = testInstallation.getSource();
    }

    @Test
    public void CRDInstallation(TestContext context) {
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "add");
        Async async = context.async();
        eventBus.<JsonObject>request(installationAddress, JsonObject.mapFrom(testInstallation), options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(testInstallation), result);
            async.complete();
        });
        async.awaitSuccess();

        JsonObject request = new JsonObject().put("_id", testInstallation.get_id());
        options = new DeliveryOptions().addHeader("method", "find");
        Async async2 = context.async();
        eventBus.<JsonObject>request(installationAddress, request, options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(testInstallation), result);
            async2.complete();
        });
        async2.awaitSuccess();

        request = new JsonObject().put("_id", testInstallation.get_id());
        options = new DeliveryOptions().addHeader("method", "remove");
        Async async3 = context.async();
        eventBus.<JsonObject>request(installationAddress, request, options, result -> {
            assertBusResult(HttpResponseStatus.NO_CONTENT, null, result);
            async3.complete();
        });
        async3.awaitSuccess();
    }

    @Test
    public void addInstallationWithoutId(TestContext context) {
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "add");
        Async async = context.async();
        eventBus.<JsonObject>request(installationAddress, JsonObject.mapFrom(noIdInstallation), options, result -> {
            context.assertTrue(result.succeeded());
            Installation installationResult = result.result().body().mapTo(Installation.class);
            assertLastIdCharIsDigit(installationResult.get_id(), context);
            async.complete();
        });
        async.awaitSuccess();
    }

    private void assertLastIdCharIsDigit(String id, TestContext context) {
        int lastCharIndex = id.length() - 1;
        context.assertTrue(Character.isDigit(id.charAt(lastCharIndex)));
    }
}
