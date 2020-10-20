package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import com.openklaster.common.tests.bus.FakeMessage;
import com.openklaster.common.tests.bus.FakeReply;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.openklaster.common.messages.BusMessageReplyUtils.STATUS_CODE;

@RunWith(VertxUnitRunner.class)
public class DeleteTokenManagerTest extends UserManagerTest {

    private static final String tokenKey = "token";
    private static final String methodName = "deleteToken";

    @Before
    public void setup() {
        commonSetup();
    }

    @Test
    public void testGenerateToken(TestContext context) {
        Async async = context.async();

        MultiMap headers = MultiMap.caseInsensitiveMultiMap();
        headers.add(apiTokenKey, existingUser.getUserTokens().get(0).getData());
        UserToken tokenToDelete = existingUser.getUserTokens().get(1);

        JsonObject usernameJson = new JsonObject()
                .put(usernameKey, existingUser.getUsername())
                .put(tokenKey,tokenToDelete.getData());

        FakeMessage<JsonObject> fakeMessage = FakeMessage.<JsonObject>builder().body(usernameJson).headers(headers).build();
        authenticatedUserManager.handleMessage(fakeMessage, methodName);

        Future<Pair<User, FakeReply>> result = fakeMessage.getMessageReply().compose(reply -> {
            Future<User> storedUser = userCrudRepository.get(existingUser.getUsername());
            return storedUser.map(userRes -> Pair.of(userRes, reply));
        });

        result.onComplete(handler -> {
            FakeReply reply = handler.result().getRight();
            User storedUser = handler.result().getLeft();
            String statusCode = reply.deliveryOptions().getHeaders().get(STATUS_CODE);
            context.assertEquals("200", statusCode);
            assertRemovedToken(storedUser, tokenToDelete, context);
            async.complete();
        });

        async.awaitSuccess();
    }

    private void assertRemovedToken(User storedUser, UserToken tokenToDelete, TestContext context) {
        context.assertFalse(storedUser.getUserTokens().contains(tokenToDelete));
    }
}