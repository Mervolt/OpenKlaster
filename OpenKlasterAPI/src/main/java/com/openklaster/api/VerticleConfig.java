package com.openklaster.api;

import com.openklaster.api.handler.PostHandler;
import com.openklaster.api.model.Login;
import com.openklaster.api.parser.DefaultParseStrategy;
import com.openklaster.api.properties.EndpointRouteProperties;
import com.openklaster.api.properties.EventBusAddressProperties;
import com.openklaster.api.properties.EventbusMethods;
import io.vertx.core.json.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;

@Configuration
@ComponentScan
public class VerticleConfig {
    private JsonObject jsonObject;

    public VerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("OpenKlasterAPI\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonObject = new JsonObject(jsonSimple);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public String buildEndpoint(int version, String route) {
        return jsonObject.getJsonObject("http").getString("prefix") +
                "/" + version + route;
    }

    @Bean
    public PostHandler postHandler() {
        return new PostHandler(buildEndpoint(jsonObject.getInteger("version"), jsonObject.getJsonObject("http").getJsonObject("endpoint").getJsonObject("route").getString("login")),
                jsonObject.getJsonObject("core").getJsonObject("route").getString("user"), EventbusMethods.LOGIN,
                new DefaultParseStrategy<Login>(Login.class));
    }
}
