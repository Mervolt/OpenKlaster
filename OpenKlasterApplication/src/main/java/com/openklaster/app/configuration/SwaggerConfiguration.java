package com.openklaster.app.configuration;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select().apis(RequestHandlerSelectors.withClassAnnotation(Hidden.class).negate()
                        .and(RequestHandlerSelectors.withClassAnnotation(RestController.class))).build()
                .globalRequestParameters(Collections.singletonList(headerParam()))
                .apiInfo(metaData());
    }

    private RequestParameter headerParam() {
        return new RequestParameterBuilder()
                .name("apiToken")
                .query(q -> q.model(modelSpecificationBuilder -> modelSpecificationBuilder.scalarModel(ScalarType.STRING)))
                .in(ParameterType.QUERY)
                .required(false)
                .build();
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Swagger OpenKlaster")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}
