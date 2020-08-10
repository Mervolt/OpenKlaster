package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import com.openklaster.common.tests.bus.FakeMessage;
import com.openklaster.common.tests.bus.FakeReply;
import com.openklaster.common.tests.model.UserBuilder;
import com.openklaster.common.tests.model.UserTestUtil;
import com.openklaster.core.vertx.service.AuthManager;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static com.openklaster.common.messages.BusMessageReplyUtils.STATUS_CODE;

@RunWith(VertxUnitRunner.class)
public class AuthenticationManagerTest extends UserManagerTest{

    private static final String methodName = "info";

    private static final User emptyUser = UserBuilder.of("empty").build();
    private static final User noSessionTokenUser = UserBuilder.of("noSession")
            .addApiToken(new UserToken("test"))
            .build();
    private static final User noApiTokenUser =  UserBuilder.of("noSession")
            .setSessionToken(new SessionToken("test", LocalDateTime.now().plusDays(1)))
            .build();
    private static final User expiredSessionTokenUser = UserBuilder.of("expired")
            .setSessionToken(new SessionToken("test",LocalDateTime.now().minusMinutes(1)))
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
    public void testUserWithoutApiToken(TestContext context) {
        testUserWithoutToken(context,apiTokenKey);
    }

    @Test
    public void testUserWithoutSessionToken(TestContext context) {
        testUserWithoutToken(context,sessionTokenKey);
    }

    private void testUserWithoutToken(TestContext context, String tokenKey) {
        Async async = context.async();

        MultiMap headers = MultiMap.caseInsensitiveMultiMap();
        headers.add(tokenKey, "XD");
        JsonObject messageBody = new JsonObject()
                .put(usernameKey, emptyUser.getUsername());
        FakeMessage<JsonObject> fakeMessage = FakeMessage.<JsonObject>builder().body(messageBody).headers(headers).build();
        authenticatedUserManager.handleMessage(fakeMessage, methodName);

        Future<FakeReply> result = fakeMessage.getMessageReply();

        result.onComplete(handler -> {
            FakeReply reply = handler.result();
            int statusCode = reply.errorCode();
            context.assertEquals(401, statusCode);
            async.complete();
        });
        async.awaitSuccess();
    }
}
