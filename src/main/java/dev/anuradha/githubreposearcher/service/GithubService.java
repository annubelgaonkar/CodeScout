package dev.anuradha.githubreposearcher.service;

import dev.anuradha.githubreposearcher.dto.GithubApiResponseDTO;
import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;
import dev.anuradha.githubreposearcher.model.RepoEntity;
import dev.anuradha.githubreposearcher.repository.RepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final WebClient githubWebClient;
    private final RepoRepository repoRepository;

    public List<RepoResponseDTO> searchAndSaveRepos(GithubSearchRequestDTO requestDTO){
        try{
            //build query
            StringBuilder queryBuilder = new StringBuilder(requestDTO.getQuery());
            if(requestDTO.getLanguage() != null && !requestDTO.getLanguage().isEmpty()){
                queryBuilder.append("+language:").append(requestDTO.getLanguage());
            }

            //call GitHub API
            GithubApiResponseDTO apiResponseDTO = githubWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("q", queryBuilder.toString())
                            .queryParam("sort", requestDTO.getSort() != null ? requestDTO.getSort() : "stars")
                            .build())
                    .retrieve()
                    .bodyToMono(GithubApiResponseDTO.class)
                    .block();

            if(apiResponseDTO == null || apiResponseDTO.getItems() == null){
                return List.of();
            }

            //Map and save / update to database
            List<RepoEntity> entities = apiResponseDTO.getItems().stream()
                    .map(item -> RepoEntity.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDesciption())
                            .ownerName(item.getOwner.getLogin())
                            .language(item.getLanguage())
                            .stars(item.getStars())
                            .forks(item.getForks())
                            .lastUpdated(item.getLastUpdated())
                            .build())
                    .collect(Collectors.toList());

            //Save all or update if already exists
            repoRepository.saveAll(entities);

            return entities.stream()
                    .map(e -> RepoResponseDTO.builder()
                            .id(e.getId())
                            .name(e.getName())
                            .description(e.getDescription())
                            .ownerName(e.getOwnerName())
                            .language(e.getLanguage())
                            .stars(e.getStars())
                            .forks(e.getForks())
                            .lastUpdated(e.getLastUpdated())
                            .build())
                    .collect(Collectors.toList());
        } catch (WebClientResponseException ex){
            throw new RuntimeException("GitHub API error: " + ex.getMessage(), ex);
        }
    }
}
