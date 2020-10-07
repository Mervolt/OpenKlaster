package com.openklaster.common.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ConfigFilesManager {

    ConfigRetrieverOptions configRetrieverOptions;
    private static final String fileType = "file";
    private static final String propertiesFormat = "properties";
    private static final String yamlFormat = "yaml";
    private static final String jsonFormat = "json";
    private static final String configPathKey = "path";
    private static final String fullFilename = "%s.%s";

    public ConfigFilesManager(String prefix) {
        this(prefix, jsonFormat);
    }

    public ConfigFilesManager(String prefix, String format) {
        this.configRetrieverOptions = addConfigFromFile(prefix, format);
    }

    public ConfigRetrieverOptions addConfigFromFile(String prefix, String format) {
        ConfigStoreOptions fileStoreOptions = new ConfigStoreOptions()
                .setType(fileType)
                .setFormat(format)
                .setConfig(new JsonObject().put(configPathKey, String.format(fullFilename, prefix, format)));
        return new ConfigRetrieverOptions().addStore(fileStoreOptions);
    }

    public ConfigRetriever getConfig(Vertx vertx) {
        return ConfigRetriever.create(vertx, this.configRetrieverOptions);
    }
}
