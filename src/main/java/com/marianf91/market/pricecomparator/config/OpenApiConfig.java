package com.marianf91.market.pricecomparator.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Price Comparator API")
                        .version("1.0.0")
                        .description("API for comparing the prices in different stores.")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                );
    }

    @Bean
    public OpenApiCustomizer globalHeaderOpenApiCustomizer() {  // renamed return type
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(operation ->
                            operation.addParametersItem(new Parameter()
                                    .in("header")
                                    .name("X-Request-Id")
                                    .description("Unique request identifier")
                                    .required(false)
                            )
                    )
            );
        };
    }
}
