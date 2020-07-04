package openklaster.common.config;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NestedConfigAccessorTest {

    @Test
    public void testGetString() {
        NestedConfigAccessor configAccessor =
                new NestedConfigAccessor(new JsonObject().put("key1", new JsonObject().put("key2","OUTP")));
        assertEquals("OUTP",configAccessor.getString("key1.key2"));

    }

    @Test
    public void testGetJsonObject(){
        JsonObject testObject = new JsonObject().put("test","test");
        NestedConfigAccessor configAccessor =
                new NestedConfigAccessor(new JsonObject().put("key1",
                        new JsonObject().put("key2", testObject)));
        assertEquals(testObject,configAccessor.getJsonObject("key1.key2"));
    }

    @Test
    public void testGetInteger() {
        NestedConfigAccessor configAccessor =
                new NestedConfigAccessor(new JsonObject().put("key1", new JsonObject().put("key2",17)));
        assertEquals(17,configAccessor.getInteger("key1.key2"));
    }

}