package com.openklaster.core.app;

public class CoreVerticleProperties {

    static final String userConfigPath = "eventbus.in.userManagement";
    static final String tokenConfigPath = "security.tokens";

    static final String installationConfigPath = "eventbus.in.installations";

    static final String sourceMeasurementConfigPath = "eventbus.in.sourceMeasurements";
    static final String loadMeasurementConfigPath = "eventbus.in.loadMeasurements";
    static final String technicalTokenConfigPath = "security.technicalToken";

    static final String mongoUserAddressConfigPath = "eventbus.out.mongo.users.address";
    static final String mongoInstallationAddressConfigPath = "eventbus.out.mongo.installation.address";
    static final String cassandraLoadMeasurementAddressConfigPath = "eventbus.out.cassandra.loadmeasurement.address";
    static final String cassandraSourceMeasurementAddressConfigPath = "eventbus.out.cassandra.sourcemeasurement.address";

    static final String tokenGeneratorCharsPerTypeKey = "charsPerType";
    static final String sessionTokenLifetimeKey = "sessionTokenLifetime";

}
