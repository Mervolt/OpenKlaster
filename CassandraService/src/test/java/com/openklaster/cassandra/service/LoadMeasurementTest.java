package com.openklaster.cassandra.service;

import com.openklaster.common.model.LoadMeasurement;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.cassandra.Mapper;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static com.openklaster.common.tests.bus.ResponsesAssertion.assertBusResult;

public class LoadMeasurementTest extends CassandraTestBase {
    @Test
    public void testPostLoadMeasurement(TestContext context) {
        Mapper<LoadMeasurement> mapper = mappingManager.mapper(LoadMeasurement.class);

        JsonObject jsonObject = createTestMeasurement();
        DeliveryOptions options = new DeliveryOptions().addHeader("methodName", "post");

        Async async = context.async();
        eventBus.<JsonObject>request("openklaster.cassandraservice.loadmeasurement", jsonObject, options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(jsonObject), result);

            mapper.get(Collections.singletonList(parseDate(TEST_DATE)), handler -> Assert.assertNotNull(handler.result()));

            mapper.delete(Collections.singletonList(parseDate(TEST_DATE)),
                    handler -> mapper.get(Collections.singletonList(parseDate(TEST_DATE)),
                            getHandler -> Assert.assertNull(handler.result())));
            async.complete();
        });
    }

}
