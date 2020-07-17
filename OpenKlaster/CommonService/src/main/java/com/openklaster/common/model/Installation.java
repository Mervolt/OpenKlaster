package com.openklaster.common.model;

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