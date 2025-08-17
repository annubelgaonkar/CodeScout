package dev.anuradha.githubreposearcher.controller;

import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;
import dev.anuradha.githubreposearcher.model.RepoEntity;
import dev.anuradha.githubreposearcher.repository.RepoRepository;
import dev.anuradha.githubreposearcher.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;
    private final RepoRepository repoRepository;

    /**
     * Search GitHub repos and save them to the database
     */
    @PostMapping("/search")
    public List<RepoResponseDTO> searchRepos(@RequestBody GithubSearchRequestDTO requestDTO){
        return githubService.searchAndSaveRepos(requestDTO);
    }

    /**
     * Retrieve the stored repos with filters
     */

    @GetMapping("/repos")
    public List<RepoResponseDTO> getRepos(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort){

        return githubService.getStoredRepos(language,minStars,sort);

    }

}
