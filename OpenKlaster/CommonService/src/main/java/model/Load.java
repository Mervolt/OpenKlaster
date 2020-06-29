package model;

import lombok.Data;

@Data
public class Load implements Model{
    private String _id;
    private String installationId;
    private String name;
    private String description;
}
