package model;

import lombok.Data;

@Data
public class Inverter extends Model {
    private String _id;
    private String installationId;
    private String description;
    private String manufacturer;
    private String credentials;
    private String modelType;
}
