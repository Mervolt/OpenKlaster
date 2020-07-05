package openklaster.common.model;

import lombok.Data;

@Data
public class Load extends Model{
    private String _id;
    private String installationId;
    private String name;
    private String description;
}
