package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Register extends Model{
    private String username;
    private String password;
    private String email;
}
