package com.openklaster.api.model;

import lombok.Data;

@Data
public class Login extends Model{
    private String username;
    private String password;
}
