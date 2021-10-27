package com.openklaster.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        RequestParameter headerParam = new RequestParameterBuilder()
                .name("apiToken")
                .query(q -> q.model(modelSpecificationBuilder -> modelSpecificationBuilder.scalarModel(ScalarType.STRING)))
                .in(ParameterType.QUERY)
                .required(false)
                .build();

        return new Docket(DocumentationType.OAS_30)
                .globalRequestParameters(Collections.singletonList(headerParam));
    }
}
