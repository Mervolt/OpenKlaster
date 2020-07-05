package com.openklaster.mongo.model;

import lombok.Data;

@Data
public class Source {

    private Integer azimuth;
    private Integer tilt;
    private Integer capacity;
    private String description;

}
