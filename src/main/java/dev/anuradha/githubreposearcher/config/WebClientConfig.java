package dev.anuradha.githubreposearcher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient githubWebClient(
            @Value("${github.base-url}") String baseUrl,
            @Value("${github.token:}") String token) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader(HttpHeaders.USER_AGENT, "github-searcher-app")
                .defaultHeaders(h -> { if (!token.isBlank()) h.setBearerAuth(token); })
                .build();
    }
}
