package org.example.configuration;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.commons.lang3.tuple.Pair;
import org.example.exception.ErrorMessage;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String SECURITY_SCHEME_NAME = "bearerAuth";
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                )
                .addSecurityItem(
                        new SecurityRequirement().addList(SECURITY_SCHEME_NAME)
                )
                .info(new Info().title("Employee Service API"));
    }

    @Bean
    public OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {

        return openApi -> {
            Paths paths = openApi.getPaths();
            for (PathItem path : paths.values()) {
                for (Operation operation : path.readOperations()) {

                    if (Set.of(Pair.of(List.of("User Controller"),"login"))
                            .contains(Pair.of(operation.getTags(), operation.getOperationId()))){
                        continue;
                    }
                    operation.getResponses().
                            addApiResponse("401", new ApiResponse().description("Authorization required")
                                    .content(new Content()
                                            .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON.getType(),
                                                    new MediaType()
                                                            .schema(
                                                                    new io.swagger.v3.oas.models.media.Schema<ErrorMessage>()
                                                                            .$ref("#/components/schemas/ErrorMessage")))));
                }
            }
        };
    }
}
