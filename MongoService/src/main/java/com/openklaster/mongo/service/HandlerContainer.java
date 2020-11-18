package com.openklaster.mongo.service;

import com.openklaster.mongo.config.CalculatorConfig;
import com.openklaster.mongo.config.EntityConfig;
import com.openklaster.mongo.config.InstallationConfig;
import com.openklaster.mongo.config.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class HandlerContainer {
    private final CalculatorConfig calculatorConfig;
    private final InstallationConfig installationConfig;
    private final UserConfig userConfig;


    @Lazy
    @Autowired
    public HandlerContainer(CalculatorConfig calculatorConfig, InstallationConfig installationConfig, UserConfig userConfig) {
        this.calculatorConfig = calculatorConfig;
        this.installationConfig = installationConfig;
        this.userConfig = userConfig;
    }

    public List<EntityConfig> retrieveHandler() {
        return Arrays.asList(calculatorConfig, installationConfig, userConfig);
    }
}
