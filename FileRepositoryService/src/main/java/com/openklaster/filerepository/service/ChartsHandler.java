package com.openklaster.filerepository.service;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.ChartsRequest;
import com.openklaster.filerepository.properties.FileRepositoryProperties;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartsHandler extends FileRepositoryHandler<ChartsRequest> {
    private final String basePath;

    public ChartsHandler(FileSystem vertxFileSystem, String address, String basePath) {
        super(vertxFileSystem, address, LoggerFactory.getLogger(ChartsHandler.class), ChartsRequest.class);
        this.basePath = basePath;
    }

    @Override
    public void handle(Message<JsonObject> message) {
        ChartsRequest chartsRequest = parseToModel(message.body());
        String path = getPath(chartsRequest);

        vertxFileSystem.readDir(path, ar -> {
            if (ar.succeeded()) {
                Map<String, String> charts = ar.result().stream()
                        .collect(Collectors.toMap(FilenameUtils::getBaseName, this::readImage));
                logger.debug("Successful request");
                BusMessageReplyUtils.replyWithBodyAndStatus(message, JsonObject.mapFrom(charts), HttpResponseStatus.OK);
            } else {
                logger.warn(ar.cause());
                BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.INTERNAL_SERVER_ERROR, ar.cause().toString());
            }
        });
    }

    private String readImage(String path) {
        try {
            byte[] content = Files.readAllBytes(Paths.get(path));
            String encodeBase64 = Base64.getEncoder().encodeToString(content);
            return "data:image/png;base64, " + encodeBase64;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPath(ChartsRequest chartsRequest) {
        return basePath.replaceAll(FileRepositoryProperties.USERNAME_TO_REPLACE, chartsRequest.getUsername())
                .replaceAll(FileRepositoryProperties.INSTALLATION_ID_TO_REPLACE, removeInstallationPrefix(chartsRequest.getInstallationId()))
                .replaceAll(FileRepositoryProperties.DATE_TO_REPLACE, chartsRequest.getDate());

    }
}
