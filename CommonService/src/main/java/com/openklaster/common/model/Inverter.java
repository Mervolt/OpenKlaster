package com.openklaster.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inverter {

    private String description;
    private String manufacturer;
    private Credentials credentials;
    private String modelType;
}
