package com.openklaster.app.model.requests;

import lombok.Value;

@Value
public class UpdateUserRequest {
    String username;
    String password;
    String newPassword;
    String email;

}
