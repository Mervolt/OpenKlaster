package com.openklaster.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Inverter {

    private String description;
    private String manufacturer;
    private String credentials;
    private String modelType;
}
