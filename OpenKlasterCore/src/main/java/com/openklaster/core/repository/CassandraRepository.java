package com.openklaster.core.repository;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class CassandraRepository<T> implements Repository<T> {

    private final DbServiceHandler<T> dbServiceHandler;
    private final static String addKey = "post";
    private final static String getKey = "get";

    public CassandraRepository(DbServiceHandler<T> dbServiceHandler) {
        this.dbServiceHandler = dbServiceHandler;
    }

    public Future<List<T>> getFromDates(JsonObject query) {
        return  dbServiceHandler.handleWithListContent(getKey, query);
    }

    @Override
    public Future<T> add(T content) {
        return dbServiceHandler.handleWithContent(addKey, JsonObject.mapFrom(content));
    }

}
