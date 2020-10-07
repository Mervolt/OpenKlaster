package com.openklaster.common.tests.model;

import com.openklaster.common.model.*;

public class InstallationTestUtil {

    public static Load prepareLoad() {
        Load load = new Load();
        load.setDescription("loadDescr");
        load.setName("loadName");
        return load;
    }

    public static Source prepareSource() {
        Source source = new Source();
        source.setAzimuth(0);
        source.setCapacity(0);
        source.setDescription("sourceDescr");
        source.setTilt(0);
        return source;
    }

    public static Inverter prepareInverter() {
        Inverter inverter = new Inverter();
        inverter.setCredentials("invCreds");
        inverter.setDescription("invDescr");
        inverter.setManufacturer("invManufacturer");
        inverter.setModelType("invModelType");
        return inverter;
    }

    public static Installation prepareInstallation(String id) {
        Installation installation = new Installation(
                id, InstallationType.Solar, prepareInverter(), prepareLoad(), prepareSource());
        installation.setUsername("userName");
        installation.setLongitude(0D);
        installation.setLatitude(0D);
        installation.setDescription("instDescr");
        return installation;
    }
    public static Installation prepareInstallation() {
        return prepareInstallation(null);
    }
}
