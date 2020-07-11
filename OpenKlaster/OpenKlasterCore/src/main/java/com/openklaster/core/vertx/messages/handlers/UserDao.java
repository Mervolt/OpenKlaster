package com.openklaster.core.vertx.messages.handlers;

import com.openklaster.common.model.User;

public class UserDao implements  Dao<User, String> {

    @Override
    public boolean insert(User data) {
        return false;
    }

    @Override
    public User get(String id) {
        return null;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
