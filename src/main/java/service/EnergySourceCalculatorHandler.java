package service;

import io.vertx.core.logging.LoggerFactory;
import model.EnergySourceCalculator;
import parser.EntityParser;

public class EnergySourceCalculatorHandler extends EntityHandler<EnergySourceCalculator>{
    public EnergySourceCalculatorHandler(EntityParser<EnergySourceCalculator> parser, MongoPersistenceService service) {
        super(parser, service);
        logger = LoggerFactory.getLogger(EnergySourceCalculatorHandler.class);
        collectionName= "energySourceCalculators";
    }
}
