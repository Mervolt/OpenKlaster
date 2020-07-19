package com.openklaster.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Source {

    private Integer azimuth;
    private Integer tilt;
    private Integer capacity;
    private String description;

}
