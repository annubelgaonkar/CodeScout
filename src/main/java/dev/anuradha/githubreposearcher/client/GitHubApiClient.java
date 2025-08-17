package dev.anuradha.githubreposearcher.client;

import dev.anuradha.githubreposearcher.exception.GitHubApiException;

public interface GitHubApiClient {

    //Call GitHub Search API and return raw repo DTOs (paginated)

    GitHubSearchResponse searchRepositories(String query, String language,
                                            String sort, int perPage, int page) throws GitHubApiException;
}
