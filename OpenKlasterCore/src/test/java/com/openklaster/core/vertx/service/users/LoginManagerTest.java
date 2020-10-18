package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import com.openklaster.common.tests.bus.FakeMessage;
import com.openklaster.common.tests.bus.FakeReply;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static com.openklaster.common.messages.BusMessageReplyUtils.STATUS_CODE;

@RunWith(VertxUnitRunner.class)
public class LoginManagerTest extends UserManagerTest {

    private LoginManager loginManager;

    @Before
    public void setup() {
        commonSetup();
        loginManager = new LoginManager(authenticationClient, userCrudRepository);
    }

    @Test
    public void testLoginSuccess(TestContext context) {
        Async async = context.async();
        context.assertNull(existingUser.getSessionToken());
        JsonObject credentials = new JsonObject()
                .put(usernameKey, existingUser.getUsername())
                .put(passwordKey, "1234");
        FakeMessage<JsonObject> fakeMessage = FakeMessage.<JsonObject>builder().body(credentials).build();
        loginManager.handleMessage(fakeMessage);

        Future<Pair<User, FakeReply>> result = fakeMessage.getMessageReply().compose(reply -> {
            Future<User> storedUser = userCrudRepository.get(existingUser.getUsername());
            return storedUser.map(userRes -> Pair.of(userRes, reply));
        });

        result.onComplete(handler -> {
            FakeReply reply = handler.result().getRight();
            User storedUser = handler.result().getLeft();
            String statusCode = reply.deliveryOptions().getHeaders().get(STATUS_CODE);
            context.assertEquals("200", statusCode);
            context.assertNotNull(storedUser.getSessionToken());
            assertSessionTokenInResult(storedUser.getSessionToken(), reply.body(), context);
            assertSessionTokenExpiration(storedUser.getSessionToken(), context);
            async.complete();
        });
        async.awaitSuccess();
    }

    @Test
    public void testLoginFail(TestContext context) {
        Async async = context.async();
        context.assertNull(existingUser.getSessionToken());
        JsonObject credentials = new JsonObject()
                .put(usernameKey, existingUser.getUsername())
                .put(passwordKey, "1235");
        FakeMessage<JsonObject> fakeMessage = FakeMessage.<JsonObject>builder().body(credentials).build();
        loginManager.handleMessage(fakeMessage);

        Future<Pair<User, FakeReply>> result = fakeMessage.getMessageReply().compose(reply -> {
            Future<User> storedUser = userCrudRepository.get(existingUser.getUsername());
            return storedUser.map(userRes -> Pair.of(userRes, reply));
        });

        result.onComplete(handler -> {
            FakeReply reply = handler.result().getRight();
            User storedUser = handler.result().getLeft();
            String cause = reply.cause();
            context.assertNull(storedUser.getSessionToken());
            context.assertEquals(cause, String.format("Cannot login - Incorrect password for user %s.", existingUser.getUsername()));
            async.complete();
        });
        async.awaitSuccess();
    }

    private void assertSessionTokenInResult(SessionToken expectedToken, Object body, TestContext context) {
        JsonObject result = JsonObject.mapFrom(body).getJsonObject(sessionTokenKey);
        SessionToken actualToken = result.mapTo(SessionToken.class);
        context.assertEquals(expectedToken, actualToken);
    }

    private void assertSessionTokenExpiration(SessionToken userToken, TestContext context) {
        LocalDateTime expirationDate = userToken.getExpirationDate();
        LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(tokenHandlerArg);
        context.assertTrue(expirationDate.isBefore(expiredDate));
    }

}