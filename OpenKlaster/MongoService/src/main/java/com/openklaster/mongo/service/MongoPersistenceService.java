package com.openklaster.mongo.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import io.vertx.ext.mongo.MongoClientUpdateResult;

public class MongoPersistenceService {

    private MongoClient client;

    public MongoPersistenceService(MongoClient client){
        this.client=client;
    }

    public void upsertByQuery(JsonObject upsertQuery, String collectionName, Handler<AsyncResult<String>> resultHandler){
        client.save(collectionName, upsertQuery, resultHandler);
    }

    public void findOneByQuery(JsonObject findQuery, String collectionName, Handler<AsyncResult<JsonObject>> resultHandler ){
         client.findOne(collectionName,findQuery,null,resultHandler);
    }

    public void removeByQuery(JsonObject removeQuery, String collectionName,Handler<AsyncResult<MongoClientDeleteResult>> resultHandler ){
        client.removeDocuments(collectionName,removeQuery,resultHandler);
    }

    public void insertByQuery(JsonObject insertQuery, String collectionName, Handler<AsyncResult<String>> resultHandler){
        client.insert(collectionName,insertQuery, resultHandler);
    }

    public void updateByQuery(JsonObject updateQuery, JsonObject updateData, String collectionName, Handler<AsyncResult<MongoClientUpdateResult>> resultHandler){
        client.updateCollection(collectionName, updateQuery,updateData, resultHandler);
    }


}
