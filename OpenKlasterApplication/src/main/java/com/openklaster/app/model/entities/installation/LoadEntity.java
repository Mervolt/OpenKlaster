package com.openklaster.app.model.entities.installation;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoadEntity {
    String name;
    String description;
}
