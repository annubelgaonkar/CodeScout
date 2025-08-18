package dev.anuradha.githubreposearcher.client;

import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GithubApiClientImpl implements GithubApiClient{

    private final WebClient githubWebClient;

    @Override
    public List<RepoResponseDTO> searchRepositories(GithubSearchRequestDTO request) {
        // call GitHub API using WebClient
        // map the response to List<RepoResponseDTO>
        return List.of(); // placeholder, implement mapping
    }
}
