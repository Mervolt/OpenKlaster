package com.openklaster.core.service;

import com.openklaster.common.model.User;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface UserRetriever {

    Future<User> retrieveUser(JsonObject entity);
}
