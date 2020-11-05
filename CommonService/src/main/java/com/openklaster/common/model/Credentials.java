package com.openklaster.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.openklaster.common.model.deserializers.CredentialsDeserializer;
import com.openklaster.common.model.serializers.CredentialsSerializer;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = CredentialsSerializer.class)
@JsonDeserialize(using = CredentialsDeserializer.class)
public class Credentials {
    JsonObject content;
}
