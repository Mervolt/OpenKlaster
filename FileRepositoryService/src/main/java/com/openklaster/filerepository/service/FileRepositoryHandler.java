package com.openklaster.filerepository.service;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.filerepository.properties.FileRepositoryProperties;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import lombok.Data;

@Data
public abstract class FileRepositoryHandler<T> {
    protected final FileSystem vertxFileSystem;
    protected final NestedConfigAccessor config;
    protected final Logger logger;
    protected final String address;
    protected final Class<T> modelClass;

    public FileRepositoryHandler(FileSystem vertxFileSystem, NestedConfigAccessor config, Logger logger, Class<T> modelClass) {
        this.vertxFileSystem = vertxFileSystem;
        this.config = config;
        this.logger = logger;
        this.modelClass = modelClass;
        this.address = config.getString(FileRepositoryProperties.ADDRESS);
    }

    abstract public void createGetHandler(Message<JsonObject> message);

    public T parseToModel(JsonObject jsonObject) {
        return jsonObject.mapTo(this.modelClass);
    }
}
