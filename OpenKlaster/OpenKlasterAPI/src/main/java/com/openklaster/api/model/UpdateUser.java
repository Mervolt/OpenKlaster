package com.openklaster.api.model;

import lombok.Data;

@Data
public class UpdateUser extends Model{
    private String username;
    private String password;
    private String newPassword;
    private String email;
}
