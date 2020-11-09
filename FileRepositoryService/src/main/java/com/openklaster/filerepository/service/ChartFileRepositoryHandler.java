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

public class ChartFileRepositoryHandler extends FileRepositoryHandler<ChartsRequest> {
    private final static String PATH = "file-repository/data/{username}/{installationId}/{date}/charts";


    public ChartFileRepositoryHandler(FileSystem vertxFileSystem, String address) {
        super(vertxFileSystem, address, LoggerFactory.getLogger(ChartFileRepositoryHandler.class), ChartsRequest.class);
    }

    @Override
    public void createGetHandler(Message<JsonObject> message) {
        ChartsRequest chartsRequest = parseToModel(message.body());
        String path = getPath(chartsRequest);

        vertxFileSystem.readDir(path, ar -> {
            if (ar.succeeded()) {
                Map<String, String> charts = ar.result().stream()
                        .collect(Collectors.toMap(FilenameUtils::getBaseName, this::readImage));
                logger.debug("Successful request");
                BusMessageReplyUtils.replyWithBodyAndStatus(message, JsonObject.mapFrom(charts), HttpResponseStatus.OK);
            } else {
                BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unsuccessful request");
                logger.debug("Unsuccessful request");
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
        return PATH.replaceAll(FileRepositoryProperties.USERNAME_TO_REPLACE, chartsRequest.getUsername())
                .replaceAll(FileRepositoryProperties.INSTALLATION_ID_TO_REPLACE, removeInstallationPrefix(chartsRequest.getInstallationId()))
                .replaceAll(FileRepositoryProperties.DATE_TO_REPLACE, chartsRequest.getDate());

    }
}
