package com.openklaster.app.model.requests;

import lombok.Value;

@Value
public class UpdateUserAdminRequest {
    String username;
    String password;
    String email;
    String role;
    boolean active;
}
