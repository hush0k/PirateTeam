package com.hush0k.pirateTeam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Auth", new SecurityScheme()
                                .name("Bearer Auth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }

    @Bean
    public GroupedOpenApi pirateApi() {
        return GroupedOpenApi.builder()
                .group("Pirate API")
                .pathsToMatch("/api/pirates/**")
                .build();
    }

    @Bean
    public GroupedOpenApi shipApi() {
        return GroupedOpenApi.builder()
                .group("Ship API")
                .pathsToMatch("/api/ships/**")
                .build();
    }

    @Bean
    public GroupedOpenApi teamApi() {
        return GroupedOpenApi.builder()
                .group("Team API")
                .pathsToMatch("/api/teams/**")
                .build();
    }

    @Bean
    public GroupedOpenApi fleetApi() {
        return GroupedOpenApi.builder()
                .group("Fleet API")
                .pathsToMatch("/api/fleets/**")
                .build();
    }

    @Bean
    public GroupedOpenApi islandApi() {
        return GroupedOpenApi.builder()
                .group("Island API")
                .pathsToMatch("/api/islands/**")
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All API")
                .pathsToMatch("/api/**")
                .build();
    }
}