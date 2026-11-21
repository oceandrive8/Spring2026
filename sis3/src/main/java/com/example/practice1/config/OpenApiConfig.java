package com.example.practice1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI movieOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Movie Catalog API")
                .version("1.0")
                .description("CRUD, recommendations and external rating lookup for movies"));
    }

    @Bean
    GlobalOpenApiCustomizer serverErrorResponse() {
        return openApi -> openApi.getPaths().values().forEach(path ->
                path.readOperations().forEach(op -> {
                    if (!op.getResponses().containsKey("500")) {
                        op.getResponses().addApiResponse("500", new ApiResponse()
                                .description("Unexpected server error")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>()
                                                .$ref("#/components/schemas/ErrorResponse")))));
                    }
                }));
    }
}

