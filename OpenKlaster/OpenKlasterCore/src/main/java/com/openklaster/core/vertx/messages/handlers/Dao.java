package com.openklaster.core.vertx.messages.handlers;

import io.vertx.core.logging.Logger;

public interface Dao<T,K> {
    public boolean insert(T data);
    public T get(K id);
    public boolean delete(K id);
}
