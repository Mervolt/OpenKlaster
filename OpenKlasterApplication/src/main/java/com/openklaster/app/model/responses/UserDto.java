package com.openklaster.app.model.responses;

import lombok.Value;

@Value
public class UserDto {
    String username;
    String email;
    String role;
    boolean active;
}
