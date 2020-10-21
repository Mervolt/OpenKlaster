package com.openklaster.core.repository;

import io.vertx.core.Future;

public interface CrudRepository<T> extends Repository<T> {

    Future<T> update(T content);

    Future<T> get(String id);

    Future<Void> delete(T content);

    Future<Void> delete(String id);

}
