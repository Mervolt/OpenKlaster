package com.openklaster.api.model;

import lombok.Data;

@Data
public class Token extends Model {
    private String data;
    private String expires;
}
