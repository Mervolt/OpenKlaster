package config;

import model.Inverter;
import parser.InverterParsers;
import service.InverterHandler;
import service.MongoPersistenceService;

public class InverterConfig extends EntityConfig<Inverter>{

    public InverterConfig(MongoPersistenceService service, InverterParsers parser) {
        super(parser, new InverterHandler(parser,service));
        mongoPersistenceService = service;
        collectionName= "inverters";
        route="/inverters/";
    }
}
