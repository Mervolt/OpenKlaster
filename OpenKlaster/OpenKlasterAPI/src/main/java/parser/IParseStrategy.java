package parser;

import io.vertx.core.json.JsonObject;
import model.Model;

public interface IParseStrategy<T extends Model> {
    T parseToModel(JsonObject jsonObject);
    JsonObject parseToJson(T model);
}

