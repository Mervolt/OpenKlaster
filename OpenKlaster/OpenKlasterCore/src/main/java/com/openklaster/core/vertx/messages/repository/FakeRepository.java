package com.openklaster.core.vertx.messages.repository;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class FakeRepository<T> implements Repository<T> {

    private Map<String, T> modelMap;
    private final static String idKey = "_id";
    private final static String noIdMsg = "No id provided!";

    public FakeRepository() {
        this.modelMap = new HashMap<>();
    }

    @Override
    public Future<T> add(T content) {
        JsonObject obj = JsonObject.mapFrom(content);
        if (!obj.containsKey(idKey)) {
            return Future.failedFuture(noIdMsg);
        }
        modelMap.put(obj.getString(idKey), content);
        return Future.succeededFuture(content);
    }

    @Override
    public Future<T> get(String id) {
        T outp = modelMap.get(id);
        if (outp == null) {
            return Future.failedFuture(String.format("Entity Not found %s _id", id));
        }
        return Future.succeededFuture(outp);
    }

    @Override
    public Future<T> update(T content) {
        JsonObject obj = JsonObject.mapFrom(content);
        if (!obj.containsKey(idKey)) {
            return Future.failedFuture(noIdMsg);
        }
        if (!modelMap.containsKey(obj.getString(idKey))) {
            return Future.failedFuture(String.format("Entity Not found %s _id", obj.getString(idKey)));
        }
        return Future.succeededFuture(modelMap.get(obj.getString(idKey)));
    }

    @Override
    public Future<Void> delete(T content) {
        JsonObject obj = JsonObject.mapFrom(content);
        if (!obj.containsKey(idKey)) {
            return Future.failedFuture(noIdMsg);
        }
        return delete(obj.getString(idKey));
    }

    @Override
    public Future<Void> delete(String id) {
        if (!modelMap.containsKey(id)) {
            return Future.failedFuture(String.format("Entity Not found %s _id", id));
        }
        modelMap.remove(id);

        return Future.succeededFuture();
    }
}
