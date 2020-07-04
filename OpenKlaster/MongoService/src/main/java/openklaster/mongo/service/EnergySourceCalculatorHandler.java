package openklaster.mongo.service;

import openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.logging.LoggerFactory;
import openklaster.mongo.model.EnergySourceCalculator;
import openklaster.mongo.parser.EntityParser;

public class EnergySourceCalculatorHandler extends EntityHandler{
    public EnergySourceCalculatorHandler(EntityParser<EnergySourceCalculator> parser,
                                         MongoPersistenceService service,
                                         NestedConfigAccessor config) {
        super(parser, service, config);
        logger = LoggerFactory.getLogger(EnergySourceCalculatorHandler.class);
    }
}
