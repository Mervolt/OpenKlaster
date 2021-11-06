package com.openklaster.app.model.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Value
@AllArgsConstructor
public class TokenEntity {
    private String data;
}
