package com.openklaster.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Source {

    private Integer azimuth;
    private Integer tilt;
    private Integer capacity;
    private String description;

}
