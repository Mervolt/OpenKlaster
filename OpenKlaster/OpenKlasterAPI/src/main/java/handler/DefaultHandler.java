package handler;

import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import model.Model;
import parser.IParseStrategy;

import java.util.List;
import java.util.Map;

public class DefaultHandler extends Handler {

    private static String access_token = "access_token";

    public DefaultHandler(String route, EventBus eventBus, IParseStrategy<? extends Model> parseStrategy) {
        super(route, eventBus, parseStrategy);
    }

    @Override
    public void post(RoutingContext context) {
        if(isPutPostRequestInvalid(context))
            return;

        JsonObject jsonModel = context.getBodyAsJson();
        eventBus.send("openKlaster.core.request.post", jsonModel);

        handleSuccessfulRequest(context.response());
    }


    @Override
    public void get(RoutingContext context) {
        if(isGetDeleteRequestInvalid(context))
            return;

        JsonObject jsonModel = context.getBodyAsJson();
        eventBus.send("openKlaster.core.request.get", jsonModel);

        handleSuccessfulRequest(context.response());
    }

    @Override
    public void put(RoutingContext context) {
        if(isPutPostRequestInvalid(context))
            return;

        JsonObject jsonModel = context.getBodyAsJson();
        eventBus.send("openKlaster.core.request.put", jsonModel);

        handleSuccessfulRequest(context.response());
    }

    @Override
    public void delete(RoutingContext context) {
        if(isGetDeleteRequestInvalid(context))
            return;

        JsonObject jsonModel = context.getBodyAsJson();
        eventBus.send("openKlaster.core.request.delete", jsonModel);

        handleSuccessfulRequest(context.response());
    }


    private boolean isPutPostRequestInvalid(RoutingContext context){
        JsonObject jsonModel = context.getBodyAsJson();

        if(isJsonModelUnprocessable(jsonModel)) {
            handleUnprocessableRequest(context.response());
            return true;
        }
        return false;
    }

    private boolean isJsonModelUnprocessable(JsonObject jsonModel){
        return !isJsonModelValid(jsonModel);
    }

    private boolean isJsonModelValid(JsonObject jsonModel) {
        try{
            parseStrategy.parseToModel(jsonModel);
            return true;
        }
        catch(IllegalArgumentException ex){
            /*
             * some logging...
             * */
            ex.printStackTrace();
            return false;
        }
    }

    private void handleUnprocessableRequest(HttpServerResponse response){
        response.setStatusCode(422);
        response.setStatusMessage("Request entity is unprocessable for this request");
        response.end();
    }

    private void handleSuccessfulRequest(HttpServerResponse response) {
        response.setStatusCode(200);
        response.setStatusMessage("Successful request");
        response.end();
    }


    private boolean isGetDeleteRequestInvalid(RoutingContext context){
        MultiMap params = context.queryParams();
        params.remove(access_token);
        if(areRequestParamsUnprocessable(params)) {
            handleUnprocessableRequest(context.response());
            return true;
        }

        return false;
    }

    private boolean areRequestParamsUnprocessable(MultiMap modelParams){
        JsonObject jsonModel = convertMultiMapToJson(modelParams.entries());
        return isJsonModelUnprocessable(jsonModel);
    }

    private JsonObject convertMultiMapToJson(List<Map.Entry<String, String>> modelParams) {
        JsonObject jsonModel = new JsonObject();
        modelParams.forEach(entry -> jsonModel.put(entry.getKey(),entry.getValue()));
        return jsonModel;
    }

}
