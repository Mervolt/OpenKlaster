package com.openklaster.app.model.entities.installation;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class InverterEntity {
    String description;
    String manufacturer;
    String modelType;
    Map<String, String> credentials;
}
