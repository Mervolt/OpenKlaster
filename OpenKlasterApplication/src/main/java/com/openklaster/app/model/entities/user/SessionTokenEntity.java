package com.openklaster.app.model.entities.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class SessionTokenEntity {

    String data;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    LocalDateTime expirationDate;
}
