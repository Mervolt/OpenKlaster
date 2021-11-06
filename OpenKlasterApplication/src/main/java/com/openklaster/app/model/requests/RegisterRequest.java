package com.openklaster.app.model.requests;

import lombok.Value;

@Value
public class RegisterRequest {
    String username;
    String password;
    String email;
}
