package com.openklaster.common.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Installation {
    @JsonAlias({ "installationId", "_id" })
    @JsonProperty("_id")
    private String _id;
    private String username;
    private Inverter inverter;
    private Load load;
    private Source source;
    private InstallationType installationType;
    private Double longitude;
    private Double latitude;
    private String description;

    public Installation(String _id, InstallationType type, Inverter inverter, Load load, Source source) {
        this(_id, type);
        this.source = source;
        this.inverter = inverter;
        this.load = load;
    }

    public Installation(String _id, InstallationType type) {
        this._id = _id;
        this.installationType = type;
    }
}
