package com.openklaster.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Source {

    private Integer azimuth;
    private Integer tilt;
    private Integer capacity;
    private String description;

}
