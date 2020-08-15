package com.openklaster.core.vertx.messages.repository;

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
        Future<List<T>> abc = dbServiceHandler.handleWithListContent(getKey, query);
        System.out.println("getFromDates" + abc.succeeded());
        return abc;
    }

    @Override
    public Future<T> add(T content) {
        return dbServiceHandler.handleWithContent(addKey, JsonObject.mapFrom(content));
    }

}
