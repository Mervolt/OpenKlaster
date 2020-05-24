package config;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NestedConfigAccessorTest {

    @Test
    void testGetString() {
        NestedConfigAccessor configAccessor =
                new NestedConfigAccessor(new JsonObject().put("key1", new JsonObject().put("key2","OUTP")));
        assertEquals("OUTP",configAccessor.getString("key1.key2"));

    }

    @Test
    void testGetJsonObject(){
        JsonObject testObject = new JsonObject().put("test","test");
        NestedConfigAccessor configAccessor =
                new NestedConfigAccessor(new JsonObject().put("key1",
                        new JsonObject().put("key2", testObject)));
        assertEquals(testObject,configAccessor.getJsonObject("key1.key2"));
    }

    @Test
    void testGetInteger() {
        NestedConfigAccessor configAccessor =
                new NestedConfigAccessor(new JsonObject().put("key1", new JsonObject().put("key2",17)));
        assertEquals(17,configAccessor.getInteger("key1.key2"));
    }

}