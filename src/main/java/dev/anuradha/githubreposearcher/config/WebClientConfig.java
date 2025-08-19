package dev.anuradha.githubreposearcher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${github.api.base-url}")
    private String githubApiBaseUrl;

    @Bean
    public WebClient githubWebClient(){
        return WebClient.builder()
                .baseUrl(githubApiBaseUrl)
                .defaultHeader("Accept","application/vnd.github+json")
                .build();
    }

}
