package com.openklaster.core.vertx.messages.repository;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class MongoCrudRepository<T> implements CrudRepository<T> {
    private final DbServiceHandler<T> dbServiceHandler;
    private final static String idKey = "_id";
    private final static String nameKey = "username";
    private final static String addKey = "add";
    private final static String getKey = "find";
    private final static String getAllKey = "findAll";
    private final static String deleteKey = "remove";
    private final static String updateKey = "update";


    public MongoCrudRepository(DbServiceHandler<T> dbServiceHandler) {
        this.dbServiceHandler = dbServiceHandler;
    }

    @Override
    public Future<T> add(T content) {
        return dbServiceHandler.handleWithContent(addKey, JsonObject.mapFrom(content));
    }

    @Override
    public Future<T> get(String id) {
        JsonObject content = new JsonObject().put(idKey, id);
        return dbServiceHandler.handleWithContent(getKey, content);
    }

    public Future<List<T>> getByUsername(String username) {
        JsonObject content = new JsonObject().put(nameKey, username);
        return dbServiceHandler.handleWithListContent(getAllKey, content);
    }

    @Override
    public Future<T> update(T content) {
        return dbServiceHandler.handleWithContent(updateKey, JsonObject.mapFrom(content));
    }

    @Override
    public Future<Void> delete(String id) {
        JsonObject content = new JsonObject().put(idKey, id);
        return dbServiceHandler.handleVoid(deleteKey, content);
    }

    @Override
    public Future<Void> delete(T content) {
        JsonObject mappedContent = JsonObject.mapFrom(content);
        return delete(mappedContent.getString(idKey));
    }

}
