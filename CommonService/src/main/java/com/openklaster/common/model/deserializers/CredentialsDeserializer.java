package com.openklaster.common.model.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.openklaster.common.model.Credentials;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class CredentialsDeserializer extends StdDeserializer<Credentials> {
    public CredentialsDeserializer() {
        this(null);
    }

    public CredentialsDeserializer(Class<?> input) {
        super(input);
    }

    @Override
    public Credentials deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        JsonObject content = JsonObject.mapFrom(node);
        return new Credentials(content);
    }
}
