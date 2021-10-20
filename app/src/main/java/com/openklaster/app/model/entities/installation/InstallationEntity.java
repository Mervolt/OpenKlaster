package com.openklaster.app.model.entities.installation;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("installations")
@Value
@Builder
public class InstallationEntity {

    @Transient
    public static final String SEQUENCE_NAME = "counters";
    @Id
    String id;
    String username;
    InstallationType installationType;
    double longitude;
    double latitude;
    String description;
    InverterEntity inverter;
    LoadEntity load;
    SourceEntity source;

}


