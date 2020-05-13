package parser;

import io.vertx.core.json.JsonObject;
import model.User;

public class UserParser implements EntityParser<User> {

    public JsonObject toJsonObject(User user){
        return new JsonObject()
                .put("_id",user.getUsername())
                .put("password",user.getPassword());
    }

    public User toEntity(JsonObject jsonObject) {
        User outp = new User();
        outp.setPassword(jsonObject.getString("password"));
        outp.setUsername(jsonObject.getString("_id"));
        return outp;
    }

}
