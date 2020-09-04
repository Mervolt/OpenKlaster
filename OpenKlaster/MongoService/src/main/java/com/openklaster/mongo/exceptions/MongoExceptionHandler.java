package com.openklaster.mongo.exceptions;

import com.mongodb.MongoException;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.lang3.tuple.Pair;

public class MongoExceptionHandler {

    public Pair<HttpResponseStatus, String> getStatusAndMessageForException(Throwable exception) {
        if (exception instanceof MongoException) {
            MongoException mongoException = (MongoException) exception;
            return resolveMongoException(mongoException.getCode());
        } else {
            throw new IllegalArgumentException(
                    String.format("Provided Exception was not a MongoException: %s", exception));
        }
    }

    private Pair<HttpResponseStatus, String> resolveMongoException(int code) {
        MongoError error = MongoError.valueFrom(code);
        return Pair.of(error.getResponseStatus(),error.getErrorMessage());
    }

    public String getWarnLogMessage(Object id, String collectionName, String customMessage){
        return String.format("[%s %s]%s",collectionName,id.toString(),customMessage);
    }
    public String getReplyFailureMessage(Object id, String customMessage){
        return String.format("%s %s",customMessage,id.toString());
    }
}
