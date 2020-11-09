package com.openklaster.filerepository.service;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.UsernameInstallation;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartFileRepositoryHandler extends FileRepositoryHandler<UsernameInstallation> {
    public ChartFileRepositoryHandler(FileSystem vertxFileSystem, String address) {
        super(vertxFileSystem, address, LoggerFactory.getLogger(ChartFileRepositoryHandler.class), UsernameInstallation.class);
    }

    @Override
    public void createGetHandler(Message<JsonObject> message) {
        File directoryPath = new File("file-repository/data/user_423432/1/2020-09-06/charts");
        File filesList[] = directoryPath.listFiles();

        vertxFileSystem.readDir("file-repository/data/user_423432/1/2020-09-06/charts", ar -> {
            if (ar.succeeded()) {
                Map<String,String> charts = ar.result().stream()
                        .collect(Collectors.toMap(FilenameUtils::getBaseName,
                                this::readImage));
                logger.debug("Successful request");
                BusMessageReplyUtils.replyWithBodyAndStatus(message, JsonObject.mapFrom(charts), HttpResponseStatus.OK);
            } else {
                BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unsuccessful request");
                logger.debug("Unsuccessful request");
            }
        });
    }

    public String readImage(String path) {
        try {
            byte[] content = Files.readAllBytes(Paths.get(path));
            String encodeBase64 = Base64.getEncoder().encodeToString(content);
            return "data:image/png;base64, " + encodeBase64;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
