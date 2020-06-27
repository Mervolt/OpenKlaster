package model;

import lombok.Data;

@Data
public class Installation {

    private String _id;
    private String username;
    private Inverter inverter;
    private Load load;
    private Source source;
    private InstallationType installationType;
    private Double longitude;
    private Double latitude;
    private String description;
}
