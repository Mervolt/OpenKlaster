package com.openklaster.api.model;

import lombok.Data;

@Data
public class Installation extends Model {

    private String installationId;
    private String username;
    private Inverter inverter;
    private Load load;
    private Source source;
    private String installationType;
    private Double longitude;
    private Double latitude;
    private String description;
}
