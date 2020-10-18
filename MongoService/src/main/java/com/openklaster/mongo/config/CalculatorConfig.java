package com.openklaster.mongo.config;

import com.openklaster.mongo.parser.EnergySourceCalculatorParser;
import com.openklaster.mongo.service.EnergySourceCalculatorHandler;
import com.openklaster.mongo.service.MongoPersistenceService;

public class CalculatorConfig extends EntityConfig {

    public CalculatorConfig(MongoPersistenceService service,
                            EnergySourceCalculatorParser parser,
                            String collectionName,
                            String busAddress) {
        super(parser, new EnergySourceCalculatorHandler(parser, service, collectionName), collectionName, busAddress);
        mongoPersistenceService = service;
    }
}
