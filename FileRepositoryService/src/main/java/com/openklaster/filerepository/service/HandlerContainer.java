package com.openklaster.filerepository.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class HandlerContainer {
    private final ChartsHandler chartHandler;
    private final SelectableDates selectableDates;

    @Lazy
    @Autowired
    public HandlerContainer(ChartsHandler chartHandler, SelectableDates selectableDates) {
        this.chartHandler = chartHandler;
        this.selectableDates = selectableDates;
    }

    public List<FileRepositoryHandler<?>> retrieveHandlers() {
        return Arrays.asList(chartHandler, selectableDates);
    }
}
