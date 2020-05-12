package service;

import io.vertx.core.logging.LoggerFactory;
import model.Inverter;
import parser.EntityParser;

public class InverterHandler extends EntityHandler<Inverter> {
    public InverterHandler(EntityParser<Inverter> parser, MongoPersistenceService service) {
        super(parser, service);
        logger= LoggerFactory.getLogger(InverterHandler.class);
        collectionName="inverters";
    }
}
