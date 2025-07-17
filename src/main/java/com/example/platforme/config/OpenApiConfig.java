package com.example.platforme.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .info(new Info().title("Plateforme API").version("1.0"))
                        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                        .components(new Components().addSecuritySchemes("bearerAuth",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
        }

        @Bean
        public GroupedOpenApi publicApi() {
                return GroupedOpenApi.builder().group("public").pathsToMatch("/api/**").build();
        }
}
