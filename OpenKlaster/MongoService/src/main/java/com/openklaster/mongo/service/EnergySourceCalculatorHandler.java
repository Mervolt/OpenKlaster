package com.openklaster.mongo.service;

import com.openklaster.mongo.model.EnergySourceCalculator;
import com.openklaster.mongo.parser.EntityParser;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.logging.LoggerFactory;

public class EnergySourceCalculatorHandler extends EntityHandler{
    public EnergySourceCalculatorHandler(EntityParser<EnergySourceCalculator> parser,
                                         MongoPersistenceService service,
                                         NestedConfigAccessor config) {
        super(parser, service, config);
        logger = LoggerFactory.getLogger(EnergySourceCalculatorHandler.class);
    }
}
