package model;

import lombok.Data;

@Data
public class Installation {
    private String _id;
    private String username;
    private InstallationType installationType;
    private Double longtitude;
    private Double latitude;
    private String description;
}
