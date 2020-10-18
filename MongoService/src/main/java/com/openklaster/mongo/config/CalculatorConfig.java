package com.openklaster.mongo.config;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.mongo.parser.EnergySourceCalculatorParser;
import com.openklaster.mongo.service.EnergySourceCalculatorHandler;
import com.openklaster.mongo.service.MongoPersistenceService;

public class CalculatorConfig extends EntityConfig {

    public CalculatorConfig(MongoPersistenceService service,
                            EnergySourceCalculatorParser parser,
                            NestedConfigAccessor config,
                            String collectionName) {
        super(parser, new EnergySourceCalculatorHandler(parser, service, collectionName), config);
        mongoPersistenceService = service;
    }
}
