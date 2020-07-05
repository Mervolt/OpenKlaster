package com.openklaster.api.parser;

import io.vertx.core.json.JsonObject;
import openklaster.common.model.Model;

public class DefaultParseStrategy<T extends Model> implements IParseStrategy {

    private Class<T> modelClass;

    public DefaultParseStrategy(Class<T> modelClass){
        this.modelClass = modelClass;
    }

    @Override
    public T parseToModel(JsonObject jsonObject) {
        return jsonObject.mapTo(this.modelClass);
    }

    @Override
    public JsonObject parseToJson(Model model) {
        return JsonObject.mapFrom(model);
    }
}
