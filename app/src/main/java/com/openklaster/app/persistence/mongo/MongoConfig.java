package com.openklaster.app.persistence.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/*@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "DEFAULT_DB";
    }

}*/
//TODO ogarnac, bo istnienie tego pliku powoduje konflikt z Kassandrowym konfigiem
// TODO Suppressed: org.springframework.data.mapping.MappingException: At least one of the @PrimaryKeyColumn annotations must have a type of PARTITIONED
