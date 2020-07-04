package openklaster.common.config;

import io.vertx.core.json.JsonObject;

/**
 * This class is a wrapper to access configs in nested forms as they were .properties files.
 * io.vertx.openklaster.common.config.ConfigRetriever returns nested objects for yaml/json openklaster.common.config files so they cannot be accessed
 * like getValue({key1}.{key2}).
 */
public class NestedConfigAccessor {

    private final String PATH_SPLIT_REGEX;

    private final JsonObject config;

    public NestedConfigAccessor(JsonObject config) {
        this.config = config;
        this.PATH_SPLIT_REGEX = "\\.";
    }

    public NestedConfigAccessor(JsonObject config, String splitRegex) {
        this.config = config;
        this.PATH_SPLIT_REGEX = splitRegex;
    }

    public NestedConfigAccessor getPathConfigAccessor(String path) {
        return new NestedConfigAccessor(getJsonObject(path));
    }

    public JsonObject getRootConfig() {
        return this.config;
    }


    public String getString(String path) {
        Object outp = getObject(path);
        return outp instanceof String ? (String) outp : outp.toString();
    }

    public int getInteger(String path) {
        return (Integer) getObject(path);
    }

    public JsonObject getJsonObject(String path) {
        return (JsonObject) getObject(path);
    }


    public Object getObject(String path) {

        String[] split = path.split(PATH_SPLIT_REGEX);
        if (split.length == 1) {
            return this.config.getValue(split[0]);
        }
        JsonObject tmp = this.config.getJsonObject(split[0]);
        try {
            for (int i = 1; i < split.length - 1; i++) {
                tmp = tmp.getJsonObject(split[i]);
            }
        } catch (NullPointerException e) {
            return null;
        }
        return tmp.getValue(split[split.length - 1]);
    }
}
