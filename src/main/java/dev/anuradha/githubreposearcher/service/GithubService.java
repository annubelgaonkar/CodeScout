package dev.anuradha.githubreposearcher.service;

import dev.anuradha.githubreposearcher.client.GithubApiClient;
import dev.anuradha.githubreposearcher.dto.*;
import dev.anuradha.githubreposearcher.exception.InvalidRequestException;
import dev.anuradha.githubreposearcher.model.RepoEntity;
import dev.anuradha.githubreposearcher.repository.RepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final WebClient githubWebClient;
    private final RepoRepository repoRepository;
    private final GithubApiClient githubApiClient;

    /**
     * Fetch repos from GitHub API and save/update in DB
     */

    public List<RepoResponseDTO> searchAndSaveRepos(GithubSearchRequestDTO requestDTO){

        // Get repos from GitHub API
        List<RepoResponseDTO> reposFromApi = githubApiClient.searchRepositories(requestDTO);

        // Map, save, and map back to DTO
        return reposFromApi.stream()
                .map(this::mapToEntity)
                .map(repoRepository::save)   // save returns the saved entity
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    // Convert DTO to Entity
    private RepoEntity mapToEntity(RepoResponseDTO dto) {
        if (dto == null) return null;

        RepoEntity entity = new RepoEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOwnerName(dto.getOwner());
        entity.setLanguage(dto.getLanguage());
        entity.setStars(dto.getStars());
        entity.setForks(dto.getForks());
        entity.setLastUpdated(dto.getLastUpdated());
        return entity;
    }
    // Convert Entity back to DTO
    private RepoResponseDTO mapToDto(RepoEntity entity) {
        if (entity == null) return null;

        return RepoResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .owner(entity.getOwnerName())
                .language(entity.getLanguage())
                .stars(entity.getStars())
                .forks(entity.getForks())
                .lastUpdated(entity.getLastUpdated())
                .build();
        }



    // Retrieve repos from DB with optional filters & sorting
    public List<RepoResponseDTO> getStoredRepos(String language,
                                                Integer minStars,
                                                String sort){

        if (minStars != null && minStars < 0) {
            throw new InvalidRequestException("minStars must be non-negative");
        }

        List<RepoEntity> repos;

        if(language != null && minStars != null){
            repos = repoRepository.findByLanguageIgnoreCaseAndStarsGreaterThanEqual(
                    language, minStars);
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

        //  if user asked for language that doesn’t exist in DB
        if (language != null && repos.isEmpty()) {
            throw new InvalidRequestException("No repositories found for language: " + language);
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
                .owner(e.getOwnerName())
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
