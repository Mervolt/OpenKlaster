package openklaster.mongo.config;

import openklaster.common.config.NestedConfigAccessor;
import openklaster.mongo.parser.EnergySourceCalculatorParser;
import openklaster.mongo.service.EnergySourceCalculatorHandler;
import openklaster.mongo.service.MongoPersistenceService;

public class CalculatorConfig extends EntityConfig {

    public CalculatorConfig(MongoPersistenceService service,
                            EnergySourceCalculatorParser parser,
                            NestedConfigAccessor config) {
        super(parser, new EnergySourceCalculatorHandler(parser, service, config), config);
        mongoPersistenceService = service;
    }
}
