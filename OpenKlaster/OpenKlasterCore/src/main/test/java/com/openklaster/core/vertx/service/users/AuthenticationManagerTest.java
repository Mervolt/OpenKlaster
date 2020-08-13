package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import com.openklaster.common.tests.bus.FakeMessage;
import com.openklaster.common.tests.bus.FakeReply;
import com.openklaster.common.tests.model.UserBuilder;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;


@RunWith(VertxUnitRunner.class)
public class AuthenticationManagerTest extends UserManagerTest {

    private static final String methodName = "info";
    private static final String statusCodeKey = "statusCode";

    private static final User emptyUser = UserBuilder.of("empty").build();
    private static final User noSessionTokenUser = UserBuilder.of("noSession")
            .addApiToken(new UserToken("api"))
            .build();
    private static final User noApiTokenUser = UserBuilder.of("noApi")
            .setSessionToken(new SessionToken("session", LocalDateTime.now().plusDays(1)))
            .build();
    private static final User expiredSessionTokenUser = UserBuilder.of("expired")
            .setSessionToken(new SessionToken("session", LocalDateTime.now().minusMinutes(1)))
            .addApiToken(new UserToken("api"))
            .build();

    @Before
    public void setup() {
        commonSetup();
        userCrudRepository.add(emptyUser);
        userCrudRepository.add(noSessionTokenUser);
        userCrudRepository.add(noApiTokenUser);
        userCrudRepository.add(expiredSessionTokenUser);
    }

    @Test
    public void testEmptyUserWithApiTokenFail(TestContext context) {
        testUserTokenFailure(context, apiTokenKey, emptyUser, "XD");
    }

    @Test
    public void testNoApiTokenUserWithApiTokenFail(TestContext context) {
        testUserTokenFailure(context, apiTokenKey, noApiTokenUser, noApiTokenUser.getSessionToken().getData());
    }

    @Test
    public void testEmptyUserWithSessionTokenFail(TestContext context) {
        testUserTokenFailure(context, sessionTokenKey, emptyUser, "XD");
    }

    @Test
    public void testNoSessionTokenUserWithSessionTokenFail(TestContext context) {
        testUserTokenFailure(context, sessionTokenKey, noSessionTokenUser, getUserApiToken(noSessionTokenUser));
    }

    @Test
    public void testExpiredSessionTokenUserWithSessionTokenFail(TestContext context) {
        User user = expiredSessionTokenUser;
        LocalDateTime previousExpirationTime = user.getSessionToken().getExpirationDate();

        testUserTokenFailure(context, sessionTokenKey, user, user.getSessionToken().getData());

        User refreshedUser = userCrudRepository.get(user.getUsername()).result();
        context.assertEquals(previousExpirationTime, refreshedUser.getSessionToken().getExpirationDate());
    }

    @Test
    public void testUserWithSessionTokenSuccess(TestContext context) {
        User user = existingUser;
        LocalDateTime previousExpirationTime = user.getSessionToken().getExpirationDate();

        testUserTokenSuccess(context, sessionTokenKey, user, user.getSessionToken().getData());

        User refreshedUser = userCrudRepository.get(user.getUsername()).result();
        context.assertFalse(previousExpirationTime.isBefore(refreshedUser.getSessionToken().getExpirationDate()));
    }

    @Test
    public void testNoApiTokenUserWithSessionTokenSuccess(TestContext context) {
        testUserTokenSuccess(context, sessionTokenKey, noApiTokenUser, noApiTokenUser.getSessionToken().getData());
    }

    @Test
    public void testExpireSessionTokenUserWithApiTokenSuccess(TestContext context) {
        testUserTokenSuccess(context, apiTokenKey, expiredSessionTokenUser, getUserApiToken(expiredSessionTokenUser));
    }

    @Test
    public void testNoSessionTokenUserWithApiTokenSuccess(TestContext context) {
        testUserTokenSuccess(context, apiTokenKey, noSessionTokenUser, getUserApiToken(noSessionTokenUser));
    }

    @Test
    public void testUserWithApiTokenSuccess(TestContext context) {
        testUserTokenSuccess(context, apiTokenKey, existingUser, getUserApiToken(existingUser));
    }

    private void testUserTokenFailure(TestContext context, String tokenKey, User user, String tokenData) {
        Async async = context.async();

        Future<FakeReply> result = getResult(tokenKey, user, tokenData);

        result.onComplete(handler -> {
            FakeReply reply = handler.result();
            int statusCode = reply.errorCode();
            context.assertEquals(401, statusCode);
            async.complete();
        });
        async.awaitSuccess();
    }

    private void testUserTokenSuccess(TestContext context, String tokenKey, User user, String tokenData) {
        Async async = context.async();

        Future<FakeReply> result = getResult(tokenKey, user, tokenData);

        result.onComplete(handler -> {
            FakeReply reply = handler.result();
            int statusCode = getStatusFromDeliveryOptions(reply.deliveryOptions());
            context.assertEquals(200, statusCode);
            async.complete();
        });
        async.awaitSuccess();
    }

    private Future<FakeReply> getResult(String tokenKey, User user, String tokenData) {
        MultiMap headers = MultiMap.caseInsensitiveMultiMap();
        headers.add(tokenKey, tokenData);
        JsonObject messageBody = new JsonObject()
                .put(usernameKey, user.getUsername());
        FakeMessage<JsonObject> fakeMessage = FakeMessage.<JsonObject>builder().body(messageBody).headers(headers).build();
        authenticatedUserManager.handleMessage(fakeMessage, methodName);
        return fakeMessage.getMessageReply();
    }

    private int getStatusFromDeliveryOptions(DeliveryOptions options) {
        return Integer.parseInt(options.getHeaders().get(statusCodeKey));
    }

    private String getUserApiToken(User user) {
        return user.getUserTokens().get(0).getData();
    }
}
