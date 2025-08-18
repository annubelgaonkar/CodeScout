package dev.anuradha.githubreposearcher.service;
import dev.anuradha.githubreposearcher.client.GithubApiClient;
import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;
import dev.anuradha.githubreposearcher.model.RepoEntity;
import dev.anuradha.githubreposearcher.exception.GithubApiException;
import dev.anuradha.githubreposearcher.exception.InvalidRequestException;
import dev.anuradha.githubreposearcher.repository.RepoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class GithubServiceTest {

    @Autowired
    private GithubService githubService;

    @MockBean
    private RepoRepository repoRepository;

    // Assume you have a GitHub API client class that makes HTTP calls
    @MockBean
    private GithubApiClient githubApiClient;

    @Test
    void testSearchAndSaveRepos_success() {
        GithubSearchRequestDTO request = new GithubSearchRequestDTO(
                "spring boot", "java", "stars");

        RepoResponseDTO repo = RepoResponseDTO.builder()
                .id(1L)
                .name("spring-boot-example")
                .description("Demo repo")
                .owner("user123")
                .language("Java")
                .stars(200)
                .forks(50)
                .lastUpdated(OffsetDateTime.now())
                .build();

        // Mock GitHub API client
        when(githubApiClient.searchRepositories(any(GithubSearchRequestDTO.class)))
                .thenReturn(List.of(repo));

        // Mock repository save
        when(repoRepository.save(any(RepoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Call service
        List<RepoResponseDTO> result = githubService.searchAndSaveRepos(request);

        assertNotNull(result);
        assertEquals(1, result.size(), "Should return 1 repo");
        assertEquals("spring-boot-example", result.get(0).getName());
        assertEquals("user123", result.get(0).getOwner());
        assertEquals(200, result.get(0).getStars());
        assertEquals("Java", result.get(0).getLanguage());
    }

    @Test
    void testSearchAndSaveRepos_apiErrorThrowsException() {
        GithubSearchRequestDTO request = new GithubSearchRequestDTO(
                "spring boot", "java", "stars");

        when(githubApiClient.searchRepositories(any(GithubSearchRequestDTO.class)))
                .thenThrow(new GithubApiException("GitHub API failed"));

        assertThrows(GithubApiException.class, () -> githubService.searchAndSaveRepos(request));
    }

    @Test
    void testGetStoredRepos_returnsFilteredResults() {
        RepoEntity repoEntity = RepoEntity.builder()
                .id(1L)
                .name("java-project")
                .ownerName("alice")
                .language("Java")
                .stars(300)
                .forks(20)
                .lastUpdated(OffsetDateTime.now())
                .build();

        when(repoRepository.findByLanguageIgnoreCaseAndStarsGreaterThanEqual(
                "Java", 100))
                .thenReturn(List.of(repoEntity));

        List<RepoResponseDTO> result = githubService.getStoredRepos(
                "Java", 100, "stars");

        assertEquals(1, result.size());
        assertEquals("java-project", result.get(0).getName());
        assertTrue(result.get(0).getStars() >= 100);
    }

    @Test
    void testGetStoredRepos_noResults() {
        when(repoRepository.findByLanguageIgnoreCaseAndStarsGreaterThanEqual(
                "Python", 500))
                .thenReturn(List.of());

        List<RepoResponseDTO> result = githubService.getStoredRepos(
                "Python", 500, "stars");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetStoredRepos_invalidStarsThrowsException() {
        assertThrows(InvalidRequestException.class,
                () -> githubService.getStoredRepos(
                        "Java", -10, "stars"));
    }
}
