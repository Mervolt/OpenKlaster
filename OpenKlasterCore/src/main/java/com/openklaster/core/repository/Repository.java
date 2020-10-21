package com.openklaster.core.repository;

import io.vertx.core.Future;

public interface Repository<T> {
    Future<T> add(T content);
}
