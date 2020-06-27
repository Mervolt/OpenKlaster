package config;

import model.EnergySourceCalculator;
import parser.EnergySourceCalculatorParser;
import service.EnergySourceCalculatorHandler;
import service.MongoPersistenceService;

public class CalculatorConfig extends EntityConfig {

    public CalculatorConfig(MongoPersistenceService service,
                            EnergySourceCalculatorParser parser,
                            NestedConfigAccessor config) {
        super(parser, new EnergySourceCalculatorHandler(parser, service, config), config);
        mongoPersistenceService = service;
    }
}
