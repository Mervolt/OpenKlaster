package model;

import lombok.Data;

@Data
public class Source {

    private String _id;
    private Integer azimuth;
    private String inverterId;
    private Integer tilt;
    private Integer capacity;
    private String description;

}
