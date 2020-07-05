package com.openklaster.mongo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {

    @JsonProperty("_id")
    private String username;
    private String password;
    private String email;
}
