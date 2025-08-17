package dev.anuradha.githubreposearcher.service;

import dev.anuradha.githubreposearcher.dto.*;
import dev.anuradha.githubreposearcher.model.RepoEntity;
import dev.anuradha.githubreposearcher.repository.RepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final WebClient githubWebClient;
    private final RepoRepository repoRepository;

    /**
     * Fetch repos from GitHub API and save/update in DB
     */

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
                            .description(item.getDescription())
                            .ownerName(item.getOwner().getLogin())
                            .language(item.getLanguage())
                            .stars(item.getStars())
                            .forks(item.getForks())
                            .lastUpdated(item.getLastUpdated())
                            .build())
                    .collect(Collectors.toList());

            //Save all or update if already exists
            repoRepository.saveAll(entities);

            return mapToResponse(entities);

        } catch (WebClientResponseException ex){
            throw new RuntimeException("GitHub API error: " + ex.getMessage(), ex);
        }
    }


    // Retrieve repos from DB with optional filters & sorting

    public List<RepoResponseDTO> getStoredRepos(String language,
                                                Integer minStars,
                                                String sort){
        List<RepoEntity> repos;

        if(language != null && minStars != null){
            repos = repoRepository.findByLanguageIgnoreCaseAndStarsGreaterThanEqual(language, minStars);
        }
        else if(language != null){
            repos = repoRepository.findByLanguageIgnoreCase(language);
        }
        else if (minStars != null) {
            repos = repoRepository.findByStarsGreaterThanEqual(minStars);
        }
        else {
            repos = repoRepository.findAll();
        }

        Comparator<RepoEntity> comparator;
        switch (sort.toLowerCase()) {
            case "forks":
                comparator = Comparator.comparing(RepoEntity::getForks,
                        Comparator.nullsLast(Integer::compareTo)).reversed();
                break;
            case "updated":
                comparator = Comparator.comparing(RepoEntity::getLastUpdated,
                        Comparator.nullsLast((a, b) ->
                                a.compareTo(b))).reversed();
                break;
            default:
                comparator = Comparator.comparing(RepoEntity::getStars,
                        Comparator.nullsLast(Integer::compareTo)).reversed();
        }
        return repos.stream()
                .sorted(comparator)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RepoResponseDTO mapToResponse(RepoEntity e) {
        return RepoResponseDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .ownerName(e.getOwnerName())
                .language(e.getLanguage())
                .stars(e.getStars())
                .forks(e.getForks())
                .lastUpdated(e.getLastUpdated())
                .build();
    }

    private List<RepoResponseDTO> mapToResponse(List<RepoEntity> entities) {
        return entities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
}
