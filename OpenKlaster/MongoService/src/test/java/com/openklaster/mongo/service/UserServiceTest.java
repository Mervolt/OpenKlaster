package com.openklaster.mongo.service;

import com.openklaster.common.model.User;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.openklaster.common.tests.bus.ResponsesAssertion.assertBusFail;
import static com.openklaster.common.tests.bus.ResponsesAssertion.assertBusResult;
import static com.openklaster.common.tests.model.UserTestUtil.prepareUser;

@RunWith(VertxUnitRunner.class)
public class UserServiceTest extends MongoServiceTest {

    private static final String userAddress = "mongo.users";
    private User testUser;
    private String notFoundUserName = "soHardNameToFind";
    private User alreadyExistsUser;
    private User updatedUser;

    @Before
    public void setupUser() {
        this.testUser = prepareUser("test1");
        this.updatedUser = prepareUser("test1");
        this.updatedUser.setEmail("ANOTHER@mail.com");
        this.alreadyExistsUser = prepareUser("alreadyExistUser");
    }

    @Test
    public void testUpdateUser(TestContext context) {
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "add");
        Async async = context.async();
        eventBus.<JsonObject>request(userAddress, JsonObject.mapFrom(testUser), options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(testUser), result);
            async.complete();
        });
        async.awaitSuccess(testTimeoutMillis);


        JsonObject findRequest = new JsonObject().put("_id", testUser.getUsername());
        options = new DeliveryOptions().addHeader("method", "find");
        Async async2 = context.async();
        eventBus.<JsonObject>request(userAddress, findRequest, options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(testUser), result);
            async2.complete();
        });
        async2.awaitSuccess(testTimeoutMillis);

        options = new DeliveryOptions().addHeader("method","update");
        Async async3 = context.async();
        eventBus.<JsonObject>request(userAddress, JsonObject.mapFrom(updatedUser), options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(updatedUser), result);
            async3.complete();
        });
        async3.awaitSuccess(testTimeoutMillis);

        options = new DeliveryOptions().addHeader("method","find");
        Async async4 = context.async();
        eventBus.<JsonObject>request(userAddress, findRequest, options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(updatedUser), result);
            context.assertNotEquals(testUser,updatedUser);
            async4.complete();
        });
        async4.awaitSuccess(testTimeoutMillis);

        options = new DeliveryOptions().addHeader("method", "remove");
        Async async5 = context.async();
        eventBus.<JsonObject>request(userAddress, findRequest, options, result -> {
            assertBusResult(HttpResponseStatus.OK, prepareRemovedEntities(1), result);
            async5.complete();
        });
        async5.awaitSuccess(testTimeoutMillis);
    }

    @Test
    public void testUserNotFoundOnFind(TestContext context) {
        JsonObject request = new JsonObject().put("_id", notFoundUserName);
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "find");
        Async async2 = context.async();
        eventBus.<JsonObject>request(userAddress, request, options, result -> {
            assertBusFail(HttpResponseStatus.NOT_FOUND, result);
            async2.complete();
        });
        async2.awaitSuccess(testTimeoutMillis);
    }

    @Test
    public void testUserNotFoundOnDelete(TestContext context) {
        JsonObject request = new JsonObject().put("_id", notFoundUserName);
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "remove");
        Async async2 = context.async();
        eventBus.<JsonObject>request(userAddress, request, options, result -> {
            assertBusResult(HttpResponseStatus.OK, prepareRemovedEntities(0), result);
            async2.complete();
        });
        async2.awaitSuccess(testTimeoutMillis);
    }

    @Test
    public void testAddAlreadyExistingUser(TestContext context) {
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "add");
        Async async = context.async();
        eventBus.<JsonObject>request(userAddress, JsonObject.mapFrom(alreadyExistsUser), options, result -> {
            async.complete();
        });

        async.awaitSuccess();
        Async async2 = context.async();

        eventBus.<JsonObject>request(userAddress, JsonObject.mapFrom(alreadyExistsUser), options, result -> {
            assertBusFail(HttpResponseStatus.BAD_REQUEST, userAlreadyExistsMsg(alreadyExistsUser.getUsername()), result);
            async2.complete();
        });
        async2.await();

        JsonObject request = new JsonObject().put("_id", alreadyExistsUser.getUsername());
        DeliveryOptions options2 = new DeliveryOptions().addHeader("method", "remove");
        Async async3 = context.async();
        eventBus.<JsonObject>request(userAddress, request, options2, result -> {
            async3.complete();
        });
        async3.awaitSuccess();
    }

    private String userAlreadyExistsMsg(String userName) {
        return String.format("Problem with adding entity. Duplicated key - %s", userName);
    }

    @Test
    public void testCRDUser(TestContext context) {
        DeliveryOptions options = new DeliveryOptions().addHeader("method", "add");
        Async async = context.async();
        eventBus.<JsonObject>request(userAddress, JsonObject.mapFrom(testUser), options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(testUser), result);
            async.complete();
        });
        async.awaitSuccess(testTimeoutMillis);

        JsonObject request = new JsonObject().put("_id", testUser.getUsername());
        options = new DeliveryOptions().addHeader("method", "find");
        Async async2 = context.async();
        eventBus.<JsonObject>request(userAddress, request, options, result -> {
            assertBusResult(HttpResponseStatus.OK, JsonObject.mapFrom(testUser), result);
            async2.complete();
        });
        async2.awaitSuccess(testTimeoutMillis);

        options = new DeliveryOptions().addHeader("method", "remove");
        Async async3 = context.async();
        eventBus.<JsonObject>request(userAddress, request, options, result -> {
            assertBusResult(HttpResponseStatus.OK, prepareRemovedEntities(1), result);
            async3.complete();
        });
        async3.awaitSuccess(testTimeoutMillis);
    }
}