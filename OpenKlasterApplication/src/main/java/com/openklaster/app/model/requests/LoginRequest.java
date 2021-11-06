package com.openklaster.app.model.requests;

import lombok.Value;

@Value
public class LoginRequest {
    String username;
    String password;
}
