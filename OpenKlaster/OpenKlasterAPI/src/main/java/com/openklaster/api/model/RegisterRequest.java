package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequest extends Model{
    private String username;
    private String password;
    private String email;
}
