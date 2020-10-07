package com.openklaster.mongo.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import io.vertx.ext.mongo.MongoClientUpdateResult;

import java.util.List;

import static com.openklaster.mongo.service.EntityHandler.ID_KEY;
import static com.openklaster.mongo.service.InstallationHandler.installationCounter;
import static com.openklaster.mongo.service.MongoQuery.getByIdQuery;
import static com.openklaster.mongo.service.MongoQuery.updateQuery;

public class MongoPersistenceService {

    private static final String countersCollectionName = "counters";
    private static final String counterValueKey = "seq";

    private MongoClient client;

    public MongoPersistenceService(MongoClient client) {
        this.client = client;
    }

    public void upsertByQuery(JsonObject upsertQuery, String collectionName, Handler<AsyncResult<String>> resultHandler) {
        client.save(collectionName, upsertQuery, resultHandler);
    }

    public void findOneByQuery(JsonObject findQuery, String collectionName, Handler<AsyncResult<JsonObject>> resultHandler) {
        client.findOne(collectionName, findQuery, null, resultHandler);
    }

    public void findAllByQuery(JsonObject findQuery, String collectionName, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        client.find(collectionName, findQuery, resultHandler);
    }


    public void removeByQuery(JsonObject removeQuery, String collectionName, Handler<AsyncResult<MongoClientDeleteResult>> resultHandler) {
        client.removeDocuments(collectionName, removeQuery, resultHandler);
    }

    public void insertByQuery(JsonObject insertQuery, String collectionName, Handler<AsyncResult<String>> resultHandler) {
        client.insert(collectionName, insertQuery, resultHandler);
    }

    public void replaceByQuery(JsonObject updateQuery, JsonObject updateData, String collectionName, Handler<AsyncResult<JsonObject>> resultHandler) {
        client.findOneAndReplace(collectionName, updateQuery, updateData, resultHandler);
    }

    public void getCounter(String counterName, Handler<AsyncResult<JsonObject>> resultHandler) {
        client.findOne(countersCollectionName, prepareGetCounterQuery(counterName), null, resultHandler);
    }

    private JsonObject prepareGetCounterQuery(String counterName) {
        return new JsonObject()
                .put(ID_KEY, counterName);
    }

    //THIS IS SO BAD TO NOT HANDLE RESULT :( but time
    // TODO quick fix so it works on clean installation
    public void updateCounter(String counterName, int value, Handler<AsyncResult<MongoClientUpdateResult>> resultHandler) {
        if (value > 1) {
            JsonObject fieldsToUpdate = new JsonObject().put(counterValueKey, value);
            JsonObject findQuery = new JsonObject().put(ID_KEY, counterName);
            client.updateCollection(countersCollectionName, findQuery, updateQuery(fieldsToUpdate), resultHandler);
        } else {
            JsonObject jsonObject = new JsonObject().put(counterValueKey, value).put(ID_KEY, counterName);
            insertByQuery(jsonObject, countersCollectionName, handler -> {
            });
        }
    }


}
