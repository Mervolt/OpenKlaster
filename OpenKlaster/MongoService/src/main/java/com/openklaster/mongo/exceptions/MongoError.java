package com.openklaster.mongo.exceptions;

import com.mchange.v2.lang.StringUtils;
import com.sun.istack.internal.NotNull;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class MongoError  {

    public static final MongoError DUPLICATE_KEY =
            newErrorCode(11000, "Duplicated key", HttpResponseStatus.BAD_REQUEST);
    public static final MongoError OTHER_ERROR =
            newErrorCode(-1,"Other error", HttpResponseStatus.INTERNAL_SERVER_ERROR);

    private static MongoError newErrorCode(int errorCode, String errorMessage, HttpResponseStatus status) {
        return new MongoError(errorCode, errorMessage, status);
    }

    @Getter
    private final String errorMessage;
    @Getter
    private final int errorCode;

    @Getter
    private final HttpResponseStatus responseStatus;

    private MongoError(@NotNull int errorCode, @NotNull String errorMessage, @NotNull HttpResponseStatus status) {
        assert (errorCode >= 0);
        assert (StringUtils.nonEmptyString(errorMessage));

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.responseStatus = status;
    }

    public static MongoError valueFrom(int errorCode) {
        switch (errorCode) {
            case 11000:
                return DUPLICATE_KEY;
            default:
                return OTHER_ERROR;
        }
    }

}
