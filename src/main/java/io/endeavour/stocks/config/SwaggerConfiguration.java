package io.endeavour.stocks.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public GroupedOpenApi usersGroup() {
        return GroupedOpenApi.builder().group("endeavour-tutorial")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
                    return operation;
                })
                .addOpenApiCustomiser(openApi ->
                        openApi.info(new Info().title("Endeavour - Spring Boot Tutorial").version("1.0"))).build();

    }
}
