package dev.anuradha.githubreposearcher.client;

public interface GitHubApiClient {

    //Call GitHub Search API and return raw repo DTOs (paginated)

    GitHubSearchResponse searchRepositories(String query, String language,
                                            String sort,
                                            int perPage, int page) throws GitHubApiException;
}
