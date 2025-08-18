package dev.anuradha.githubreposearcher.client;

import dev.anuradha.githubreposearcher.dto.GithubApiResponseDTO;
import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;
import dev.anuradha.githubreposearcher.exception.GithubApiException;
import dev.anuradha.githubreposearcher.exception.RateLimitException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GithubApiClientImpl implements GithubApiClient{
    private final WebClient githubWebClient;

    @Override
    public List<RepoResponseDTO> searchRepositories(GithubSearchRequestDTO request){
        String q = (request.getLanguage() == null || request.getLanguage().isBlank())
                ? request.getQuery()
                : request.getQuery() + " language:" + request.getLanguage();

        GithubApiResponseDTO apiResponse = githubWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", q)
                        .queryParam("sort", request.getSort()) // stars | forks | updated
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->{
        if (response.statusCode().value() == 403 || response.statusCode().value() == 429) {

            String retryAfter = response.headers().asHttpHeaders().getFirst("Retry-After");
            long retryAfterSecs = retryAfter != null ? Long.parseLong(retryAfter) : 60;
            return Mono.error(new RateLimitException("GitHub rate limit exceeded", retryAfterSecs));
        }
        return response.bodyToMono(String.class)
                .flatMap(body -> Mono.error(new GithubApiException("GitHub API error: " + body)));
    })
            .bodyToMono(GithubApiResponseDTO.class)
        .block();
        if (apiResponse == null || apiResponse.getItems() == null) {
            return List.of();
        }

        // Map items -> RepoResponseDTO
        return apiResponse.getItems().stream()
                .map(item -> RepoResponseDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .owner(item.getOwner().getLogin())
                        .language(item.getLanguage())
                        .stars(item.getStars())
                        .forks(item.getForks())
                        .lastUpdated(item.getLastUpdated())
                        .build())
                .collect(Collectors.toList());
    }

}
