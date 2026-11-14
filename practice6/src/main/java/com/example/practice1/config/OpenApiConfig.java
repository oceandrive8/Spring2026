package com.example.practice1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI movieOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Movie Catalog API")
                .version("1.0")
                .description("CRUD, recommendations and external rating lookup for movies"));
    }
}
