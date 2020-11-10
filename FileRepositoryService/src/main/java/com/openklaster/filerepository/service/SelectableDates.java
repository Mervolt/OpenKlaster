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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectableDates extends FileRepositoryHandler<SelectableDatesRequest> {
    private final static String PATH = "file-repository/data/{username}/{installationId}";

    public SelectableDates(FileSystem vertxFileSystem, String address) {
        super(vertxFileSystem, address, LoggerFactory.getLogger(ChartsHandler.class), SelectableDatesRequest.class);
    }

    @Override
    public void handle(Message<JsonObject> message) {
        SelectableDatesRequest selectableDatesRequest = parseToModel(message.body());
        String path = getPath(selectableDatesRequest);

        File directory = new File(path);
        File[] filesList = directory.listFiles();

        List<String> response = new ArrayList<>();
        if (filesList != null && filesList.length > 0) response = Arrays.stream(filesList)
                .filter(file -> file.getName().matches("\\d{4}-\\d{2}-\\d{2}"))
                .filter(File::isDirectory)
                .map(File::getName)
                .collect(Collectors.toList());

        BusMessageReplyUtils.replyWithBodyAndStatus(message, new JsonArray(response), HttpResponseStatus.OK);
    }

    private String getPath(SelectableDatesRequest selectableDatesRequest) {
        return PATH.replaceAll(FileRepositoryProperties.USERNAME_TO_REPLACE, selectableDatesRequest.getUsername())
                .replaceAll(FileRepositoryProperties.INSTALLATION_ID_TO_REPLACE, removeInstallationPrefix(selectableDatesRequest.getInstallationId()));

    }
}
