package com.openklaster.app.model.requests;

import com.openklaster.app.model.entities.installation.InstallationType;
import com.openklaster.app.model.entities.installation.InverterEntity;
import com.openklaster.app.model.entities.installation.LoadEntity;
import com.openklaster.app.model.entities.installation.SourceEntity;
import lombok.Value;

import java.util.Map;

@Value
public class InstallationUpdateRequest {
    String installationId;
    String username;
    InstallationType installationType;
    double longitude;
    double latitude;
    String description;
    LoadEntity load;
    InverterEntity inverter;
    SourceEntity source;
}
