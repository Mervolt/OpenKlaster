package com.openklaster.app.model.requests;

import com.openklaster.app.model.entities.installation.InstallationType;
import com.openklaster.app.model.entities.installation.InverterEntity;
import com.openklaster.app.model.entities.installation.LoadEntity;
import com.openklaster.app.model.entities.installation.SourceEntity;
import com.openklaster.app.validation.installation.SafeInstallation;
import lombok.Value;

@Value
public class InstallationUpdateRequest {
    @SafeInstallation
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
