package com.openklaster.common.model.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.openklaster.common.model.Credentials;

import java.io.IOException;

public class CredentialsSerializer extends StdSerializer<Credentials> {

    public CredentialsSerializer() {
        this(null);
    }

    public CredentialsSerializer(Class<Credentials> t) {
        super(t);
    }

    @Override
    public void serialize(Credentials credentials, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        credentials.getContent().fieldNames().forEach(fieldName ->
        {
            try {
                jsonGenerator.writeStringField(fieldName, credentials.getContent().getString(fieldName));
            } catch (IOException exception) {
                throw new IllegalArgumentException("Cannot serialize credentials to json:" + credentials.getContent());
            }
        });
        jsonGenerator.writeEndObject();
    }
}
