package dev.anuradha.githubreposearcher.client;


import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;

import java.util.List;

public interface GithubApiClient {
    List<RepoResponseDTO> searchRepositories(GithubSearchRequestDTO request);
}
