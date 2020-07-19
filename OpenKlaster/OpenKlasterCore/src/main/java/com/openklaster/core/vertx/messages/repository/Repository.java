package com.openklaster.core.vertx.messages.repository;

import io.vertx.core.Future;

public interface Repository<T> {

    Future<T> add(T content);

    Future<T> get(String id);

    Future<T> update(T content);

    Future<Void> delete(T content);

    Future<Void> delete(String id);

}
