package service;

import app.MongoVerticle;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import model.User;
import openklaster.common.config.ConfigFilesManager;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import parser.UserParser;

@RunWith(VertxUnitRunner.class)
@FixMethodOrder()
public class UserServiceTest {

    @Rule
    public final RunTestOnContext vertxRule = new RunTestOnContext();

    private static Vertx vertx;
    private static EventBus eventBus;
    private static UserParser parser;
    private static final String userAddress= "mongo.users";
    private User testUser;

    @Before
    public void setup(TestContext context) {
        ConfigFilesManager configFilesManager = new ConfigFilesManager();
        Async async = context.async();
        vertx = Vertx.vertx();
        MongoVerticle myMongoVerticle = new MongoVerticle(vertx, configFilesManager.getConfig(vertx));
        vertx.deployVerticle(myMongoVerticle, result -> async.complete());
        eventBus = vertx.eventBus();
        prepareUser();

    }

    private void prepareUser() {
        testUser = new User();
        testUser.setUsername("test1");
        testUser.setPassword("1234");
        testUser.setEmail("test@test.com");
    }

    @Test
    public void testCreateUser(TestContext context) {
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "add");
        Async async = context.async();
        eventBus.<JsonObject>request(userAddress, JsonObject.mapFrom(testUser), options, result -> {
            context.assertTrue(result.succeeded());
            context.assertEquals(result.result().body().mapTo(User.class), testUser);
            context.assertEquals(result.result().headers().get("statusCode"),
                    getStatusAsString(HttpResponseStatus.OK));
            async.complete();
        });
    }

    @Test
    public void testGetUser(TestContext context) {
        JsonObject request = new JsonObject().put("_id", testUser.getUsername());
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "find");
        Async async = context.async();
        eventBus.<JsonObject>request(userAddress, request, options, result -> {
            context.assertTrue(result.succeeded());
            context.assertEquals(result.result().body().mapTo(User.class), testUser);
            context.assertEquals(result.result().headers().get("statusCode"),
                    getStatusAsString(HttpResponseStatus.OK));
            async.complete();
        });
    }

    @Test
    public void testDeleteUser(TestContext context) {
        JsonObject request = new JsonObject().put("_id", testUser.getUsername());
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "remove");
        Async async = context.async();
        eventBus.<JsonObject>request(userAddress, request, options, result -> {
            context.assertTrue(result.succeeded());
            context.assertNull(result.result().body());
            context.assertEquals(result.result().headers().get("statusCode"),
                    getStatusAsString(HttpResponseStatus.NO_CONTENT));
            async.complete();
        });
    }

    private String getStatusAsString(HttpResponseStatus status){
        return String.valueOf(status.code());
    }
}