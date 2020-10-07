package com.openklaster.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Inverter {

    private String description;
    private String manufacturer;
    private String credentials;
    private String modelType;
}
