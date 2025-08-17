package dev.anuradha.githubreposearcher.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {
    @Bean
    public OpenAPI api(){
        return new OpenAPI().info(new Info()
                .title("GitHub Repository Searcher API")
                .version("v1")
        );
    }
}
