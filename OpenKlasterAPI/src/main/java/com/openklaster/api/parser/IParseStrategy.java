package com.openklaster.api.parser;

import io.vertx.core.json.JsonObject;
import com.openklaster.api.model.Model;

public interface IParseStrategy<T extends Model> {
    T parseToModel(JsonObject jsonObject);
}

