package com.openklaster.filerepository.service;

import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import lombok.Data;

@Data
public abstract class FileRepositoryHandler<T> {
    protected final FileSystem vertxFileSystem;
    protected final Logger logger;
    protected final String address;
    protected final Class<T> modelClass;

    public FileRepositoryHandler(FileSystem vertxFileSystem, String address, Logger logger, Class<T> modelClass) {
        this.vertxFileSystem = vertxFileSystem;
        this.logger = logger;
        this.modelClass = modelClass;
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    abstract public void createGetHandler(Message<JsonObject> message);

    public T parseToModel(JsonObject jsonObject) {
        return jsonObject.mapTo(this.modelClass);
    }

    protected String removeInstallationPrefix(String installation) {
        return installation.split(":")[1];
    }
}
