package config;

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
    private static final String defaultConfigFile = "config.yaml";

    public ConfigFilesManager(String path) {
        this.configRetrieverOptions = new ConfigRetrieverOptions();
        addConfigFromFile(path);
    }

    public ConfigFilesManager() {
        this(defaultConfigFile);
    }

    public void addConfigFromFile(String path) {
        ConfigStoreOptions fileStoreOptions = new ConfigStoreOptions()
                .setType(fileType)
                .setFormat(yamlFormat)
                .setConfig(new JsonObject().put("path", path));
        this.configRetrieverOptions.addStore(fileStoreOptions);
    }

    public ConfigRetriever getConfig(Vertx vertx) {
        return ConfigRetriever.create(vertx, this.configRetrieverOptions);
    }
}
