package model;

import lombok.Data;

@Data
public class Installation implements Model{
    private String _id;
    private String username;
    private InstallationType installationType;
    private Double longitude;
    private Double latitude;
    private String description;
}
