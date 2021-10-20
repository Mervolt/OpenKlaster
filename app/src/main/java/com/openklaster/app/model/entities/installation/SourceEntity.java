package com.openklaster.app.model.entities.installation;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SourceEntity {
    String description;
    double azimuth;
    double tilt;
    double capacity;
}
