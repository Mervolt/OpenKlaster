package com.openklaster.filerepository.service;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.UsernameInstallation;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.core.file.OpenOptions.DEFAULT_READ;

public class SelectableDates extends FileRepositoryHandler<UsernameInstallation>  {
    public SelectableDates(FileSystem vertxFileSystem, NestedConfigAccessor config) {
        super(vertxFileSystem, config, LoggerFactory.getLogger(ChartFileRepositoryHandler.class), UsernameInstallation.class);
    }

    @Override
    public void createGetHandler(Message<JsonObject> message) {
        File directoryPath = new File("file-repository/data/user_423432/1");
        File filesList[] = directoryPath.listFiles();

        List<String> a = Arrays.stream(filesList)
                .filter(file -> file.getName().matches("\\d{4}-\\d{2}-\\d{2}"))
                .filter(File::isDirectory)
                .map(File::getName)
                .collect(Collectors.toList());

        BusMessageReplyUtils.replyWithBodyAndStatus(message, new JsonArray(a), HttpResponseStatus.OK);
    }
}
