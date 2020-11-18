package com.openklaster.mongo.service;

import com.openklaster.common.model.EnergySourceCalculator;
import com.openklaster.mongo.parser.EntityParser;
import io.vertx.core.logging.LoggerFactory;

public class EnergySourceCalculatorHandler extends EntityHandler {
    public EnergySourceCalculatorHandler(EntityParser<EnergySourceCalculator> parser,
                                         MongoPersistenceService service, String collectionName) {
        super(parser, service, collectionName);
        logger = LoggerFactory.getLogger(EnergySourceCalculatorHandler.class);
    }
}
