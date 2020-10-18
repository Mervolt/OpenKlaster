package com.openklaster.cassandra.service;

import com.openklaster.cassandra.VerticleConfig;
import com.openklaster.cassandra.app.CassandraVerticle;
import com.openklaster.cassandra.properties.CassandraProperties;
import com.openklaster.common.config.ConfigFilesManager;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.MappingManager;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(VertxUnitRunner.class)
public class CassandraTestBase {
    protected final String TEST_DATE = "1990-01-01 00:00:00";
    protected Vertx vertx;
    protected EventBus eventBus;
    protected CassandraVerticle verticle;
    protected CassandraClient cassandraClient;
    protected MappingManager mappingManager;

    @Before
    public void setUp(TestContext context) {
        //IMPROVE TESTS
        GenericApplicationContext ctx = new AnnotationConfigApplicationContext(VerticleConfig.class);
        Async async = context.async();
        vertx = Vertx.vertx();
        verticle = new CassandraVerticle();

        vertx.deployVerticle(verticle, result -> {
            ctx.registerBean(Vertx.class, () -> vertx);
            cassandraClient = ctx.getBean(CassandraClient.class);
            mappingManager = MappingManager.create(cassandraClient);
            async.complete();
        });
        this.eventBus = vertx.eventBus();
    }

    @After
    public void tearDown() {
        vertx.close();
    }

    @Test
    public void testConnection(TestContext context) {
        Async async = context.async();
        cassandraClient.prepare("SELECT now() FROM system.local;", preparedStatementResult -> {
            Assert.assertTrue(preparedStatementResult.succeeded());
            async.complete();
        });
    }


    protected JsonObject createTestMeasurement() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("installationId", "test");
        jsonObject.put("timestamp", TEST_DATE);
        jsonObject.put("unit", "kW");
        jsonObject.put("value", 22.2);
        return jsonObject;
    }

    protected Date parseDate(String date) {
        SimpleDateFormat parser = new SimpleDateFormat(CassandraProperties.DATETIME_FORMAT);
        try {
            return parser.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
