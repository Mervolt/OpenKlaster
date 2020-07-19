package com.openklaster.core.vertx.messages.repository;

import io.vertx.core.Future;

public interface Repository<T> {
    Future<T> add(T content);
}
