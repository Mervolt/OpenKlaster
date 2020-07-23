package com.openklaster.cassandra.service;

import com.openklaster.cassandra.app.CassandraVerticle;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.openklaster.common.tests.bus.ResponsesAssertion.assertBusResult;
import static org.junit.Assert.*;

@RunWith(VertxUnitRunner.class)
public class LoadMeasurementHandlerTest {
    private Vertx vertx;
    private EventBus eventBus;
    private CassandraVerticle verticle;


    @Before
    public void setUp(TestContext context) {
        Async async = context.async();
        ConfigFilesManager configFilesManager = new ConfigFilesManager();
        vertx = Vertx.vertx();
        verticle = new CassandraVerticle(vertx, configFilesManager.getConfig(vertx));
        vertx.deployVerticle(verticle, result -> {
            async.complete();
        });
        this.eventBus = vertx.eventBus();
    }

    @After
    public void tearDown() {
        vertx.close();
    }

    @Test
    public void test1(TestContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("installationId", "installation:0");
        //jsonObject.put("timestampds", "2020-06-20 19:38:27.412sad");
        jsonObject.put("unit", "kW");
        jsonObject.put("value", 22.2);
        DeliveryOptions options = new DeliveryOptions().addHeader("methodName", "post");
        Async async = context.async();
        eventBus.<JsonObject>request("openklaster.cassandraservice.loadmeasurement", jsonObject, options, result -> {
            //assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(jsonObject), result);
  //          System.out.println(result.result().body());
            System.out.println(result.result());
            System.out.println(result.failed());
            System.out.println(result.cause());
            //assertTrue(!result.failed());
            System.out.println("XD");
            async.complete();
        });
    }

    @Test
    public void get(TestContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("installationId", "installation:0");
        jsonObject.put("startDate", "2020-07-22 22:29:31");
        jsonObject.put("endDate", "2020-07-24");
        DeliveryOptions options = new DeliveryOptions().addHeader("methodName", "get");
        Async async = context.async();
        eventBus.<JsonObject>request("openklaster.cassandraservice.loadmeasurement", jsonObject, options, result -> {
            //assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(jsonObject), result);
            //          System.out.println(result.result().body());
            System.out.println("result" + result.result().body());
            System.out.println(result.failed());
            System.out.println(result.cause());
            //assertTrue(!result.failed());
            System.out.println("XD");
            async.complete();
        });
    }
}
