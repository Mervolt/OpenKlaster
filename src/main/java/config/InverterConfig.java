package config;

import model.Inverter;
import parser.InverterParser;
import service.InverterHandler;
import service.MongoPersistenceService;

public class InverterConfig extends EntityConfig<Inverter>{

    public InverterConfig(MongoPersistenceService service, InverterParser parser) {
        super(parser, new InverterHandler(parser,service));
        mongoPersistenceService = service;
        collectionName= "inverters";
        route="/inverters/";
    }
}
