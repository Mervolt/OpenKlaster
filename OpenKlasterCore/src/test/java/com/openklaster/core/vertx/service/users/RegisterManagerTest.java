package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
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

import static com.openklaster.common.messages.BusMessageReplyUtils.*;

@RunWith(VertxUnitRunner.class)
public class RegisterManagerTest extends UserManagerTest {

    private RegisterManager registerManager;

    @Before
    public void setup() {
        commonSetup();
        this.registerManager = new RegisterManager(authenticationClient, userCrudRepository);
    }

    @Test
    public void testRegisterSuccess(TestContext context) {
        Async async = context.async();
        JsonObject userJson = JsonObject.mapFrom(testUser);
        userJson.remove("userTokens");
        userJson.remove("sessionToken");
        FakeMessage<JsonObject> fakeMessage = FakeMessage.<JsonObject>builder().body(userJson).build();
        this.registerManager.handleMessage(fakeMessage);

        Future<Pair<User, FakeReply>> result = fakeMessage.getMessageReply().compose(reply -> {
            Future<User> storedUser = userCrudRepository.get(testUser.getUsername());
            return storedUser.map(userRes -> Pair.of(userRes, reply));
        });

        result.onComplete(handler -> {
            FakeReply reply = handler.result().getRight();
            User storedUser = handler.result().getLeft();
            String statusCode = reply.deliveryOptions().getHeaders().get(STATUS_CODE);
            context.assertEquals("200",statusCode);
            assertCorrectPassword(testUser.getPassword(),storedUser.getPassword(),context);
            async.complete();
        });

        async.awaitSuccess();
    }

    private void assertCorrectPassword(String plainPassword, String hashedPassword, TestContext context){
        context.assertTrue(passwordHandler.authenticatePassword(plainPassword,hashedPassword));
    }

    private String duplicatedUserMsg(String username){
        return String.format("Problem with adding entity. Duplicated key - %s", username);
    }
}