package config;

import model.EnergySourceCalculator;
import parser.EnergySourceCalculatorParser;
import service.EnergySourceCalculatorHandler;
import service.MongoPersistenceService;

public class CalculatorConfig extends EntityConfig<EnergySourceCalculator> {

    public CalculatorConfig(MongoPersistenceService service, EnergySourceCalculatorParser parser) {
        super(parser, new EnergySourceCalculatorHandler(parser,service));
        mongoPersistenceService = service;
        collectionName= "calculators";
        route="/calculators/";
    }
}
