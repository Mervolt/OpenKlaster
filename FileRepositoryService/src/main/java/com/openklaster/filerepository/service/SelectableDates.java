package com.openklaster.filerepository.service;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.SelectableDatesRequest;
import com.openklaster.filerepository.properties.FileRepositoryProperties;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectableDates extends FileRepositoryHandler<SelectableDatesRequest> {
    private final String basePath;

    public SelectableDates(FileSystem vertxFileSystem, String address, String basePath) {
        super(vertxFileSystem, address, LoggerFactory.getLogger(ChartsHandler.class), SelectableDatesRequest.class);
        this.basePath = basePath;
    }

    @Override
    public void handle(Message<JsonObject> message) {
        SelectableDatesRequest selectableDatesRequest = parseToModel(message.body());
        String path = getPath(selectableDatesRequest);

        vertxFileSystem.readDir(path, ar -> {
            if (ar.succeeded()) {
                List<String> response = ar.result().stream()
                        .filter(file -> (FilenameUtils.getExtension(file).isEmpty()))
                        .map(FilenameUtils::getBaseName)
                        .collect(Collectors.toList());
                BusMessageReplyUtils.replyWithBodyAndStatus(message, new JsonArray(response), HttpResponseStatus.OK);
            }
            else {
                logger.warn(ar.cause());
                BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.INTERNAL_SERVER_ERROR, ar.cause().toString());
            }
        });
    }

    private String getPath(SelectableDatesRequest selectableDatesRequest) {
        return basePath.replaceAll(FileRepositoryProperties.USERNAME_TO_REPLACE, selectableDatesRequest.getUsername())
                .replaceAll(FileRepositoryProperties.INSTALLATION_ID_TO_REPLACE, removeInstallationPrefix(selectableDatesRequest.getInstallationId()));

    }
}
