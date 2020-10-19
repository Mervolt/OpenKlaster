package com.openklaster.filerepository.service;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.UsernameInstallation;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

import java.util.Base64;

public class ChartFileRepositoryHandler extends FileRepositoryHandler<UsernameInstallation> {
    public ChartFileRepositoryHandler(FileSystem vertxFileSystem, NestedConfigAccessor config) {
        super(vertxFileSystem, config, LoggerFactory.getLogger(ChartFileRepositoryHandler.class), UsernameInstallation.class);
    }

    @Override
    public void createGetHandler(Message<JsonObject> message) {
        vertxFileSystem.readFile("/file-repository/cat.jpg", ar -> {
            if (ar.succeeded()) {
                byte[] content = ar.result().getBytes();
                String encodeBase64 = Base64.getEncoder().encodeToString(content);
                String response = "data:image/jpg;base64, " + encodeBase64;
                logger.debug("Successful request");
                BusMessageReplyUtils.replyWithBodyAndStatus(message, response, HttpResponseStatus.OK);
            } else {
                BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unsuccessful request");
                logger.debug("Unsuccessful request");
            }
        });
    }
}
