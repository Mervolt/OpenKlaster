package com.openklaster.api.parser;

import io.vertx.core.json.JsonObject;
import com.openklaster.api.model.Model;

public class DefaultParseStrategy<T extends Model> implements IParseStrategy {

    private Class<T> modelClass;

    public DefaultParseStrategy(Class<T> modelClass){
        this.modelClass = modelClass;
    }

    @Override
    public T parseToModel(JsonObject jsonObject) {
        return jsonObject.mapTo(this.modelClass);
    }
}
