package com.openklaster.api.utils;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;

public class PrepareData {
    public static HashMap<String, Object> getInstallationJsonObject() {
        JsonObject load = new JsonObject();
        load.put("name", "string");
        load.put("description", "string");

        JsonObject source = new JsonObject();
        source.put("azimuth", 1);
        source.put("tilt", 1);
        source.put("capacity", 1);
        source.put("description", "string");

        JsonObject inverter = new JsonObject();
        inverter.put("description", "string");
        inverter.put("manufacturer", "string");
        inverter.put("credentials", "string");
        inverter.put("modelType", "string");

        HashMap<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("username", "test");
        bodyParams.put("longitude", 1);
        bodyParams.put("latitude", 1);
        bodyParams.put("installationType", "Solar");
        bodyParams.put("description", "string");
        bodyParams.put("load", load);
        bodyParams.put("source", source);
        bodyParams.put("inverter", inverter);

        return bodyParams;
    }

    public static HashMap<String, Object> getInstallationJsonObjectWithId() {
        HashMap<String, Object> bodyParams =getInstallationJsonObject();
        bodyParams.put("installationId", "installation:1");
        return bodyParams;
    }

    public static HashMap<String, Object> getApiToken() {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("apiToken", "token");
        return queryParams;
    }

    public static HashMap<String, Object> getMeasurementJsonObject() {
        HashMap<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("installationId", "installation:1");
        bodyParams.put("timestamp", "2020-07-18 20:10:08");
        bodyParams.put("value", 1.1);
        return bodyParams;
    }
}
