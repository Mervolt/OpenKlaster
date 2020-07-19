package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import com.openklaster.common.tests.bus.FakeMessage;
import com.openklaster.common.tests.bus.FakeReply;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.openklaster.common.messages.BusMessageReplyUtils.STATUS_CODE;

@RunWith(VertxUnitRunner.class)
public class InformationManagerTest extends UserManagerTest {

    @Before
    public void setup() {
        commonSetup();
        this.userManager = new InformationManager(authenticationClient, userCrudRepository);
    }

    @Test
    public void testGetInfo(TestContext context) {
        Async async = context.async();

        MultiMap headers = MultiMap.caseInsensitiveMultiMap();
        headers.add(apiTokenKey, existingUser.getUserTokens().get(0).getData());
        JsonObject messageBody = new JsonObject()
                .put(usernameKey, existingUser.getUsername());
        FakeMessage<JsonObject> fakeMessage = FakeMessage.<JsonObject>builder().body(messageBody).headers(headers).build();
        userManager.handleMessage(fakeMessage);

        Future<FakeReply> result = fakeMessage.getMessageReply();

        result.onComplete(handler -> {
            FakeReply reply = handler.result();
            String statusCode = reply.deliveryOptions().getHeaders().get(STATUS_CODE);
            context.assertEquals("200", statusCode);
            assertUserInfo(existingUser, reply.body(), context);
            async.complete();
        });
        async.awaitSuccess();
    }

    private void assertUserInfo(User existingUser, Object body, TestContext context) {
        JsonObject result = (JsonObject) body;
        context.assertEquals(existingUser.getEmail(), result.getString(emailKey));
        JsonArray userTokenList = result.getJsonArray(userTokensKey);
        userTokenList.forEach(token -> {
            UserToken tmpToken = JsonObject.mapFrom(token).mapTo(UserToken.class);
            context.assertTrue(existingUser.getUserTokens().contains(tmpToken));
        });
        context.assertEquals(existingUser.getUsername(), result.getString(usernameKey));
    }
}