package dev.anuradha.githubreposearcher.controller;

import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;
import dev.anuradha.githubreposearcher.dto.SearchResponseDTO;
import dev.anuradha.githubreposearcher.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    /**
     * Search GitHub repos and save them to the database
     */
    @PostMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchRepos(
            @RequestBody GithubSearchRequestDTO requestDTO){
       List<RepoResponseDTO> repos = githubService.searchAndSaveRepos(requestDTO);
       return ResponseEntity.ok(new SearchResponseDTO(
               "Repositories fetched and saved successfully",
               repos
       ));
    }

    /**
     * Retrieve the stored repos with filters
     */

    @GetMapping("/repositories")
    public List<RepoResponseDTO> getRepos(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort){

        return githubService.getStoredRepos(language,minStars,sort);
    }

}
