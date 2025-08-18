package dev.anuradha.githubreposearcher.service;

import com.sun.jdi.request.InvalidRequestStateException;
import dev.anuradha.githubreposearcher.dto.GithubApiResponseDTO;
import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;
import dev.anuradha.githubreposearcher.model.RepoEntity;
import dev.anuradha.githubreposearcher.repository.RepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class GithubServiceTest {

    @Autowired
    private GithubService githubService;

    @MockBean
    private WebClient githubWebClient;

    @MockBean
    private RepoRepository repoRepository;

    //mock weblclient chain
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;


    @BeforeEach
    void setUp() {
        requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(githubWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void testSearchAndSaveRepositories_returnsRepositories(){
        //Arrange
        GithubSearchRequestDTO request = new GithubSearchRequestDTO("spring boot","Java","stars");

        GithubApiResponseDTO.RepoItem repoItem = GithubApiResponseDTO.RepoItem.builder()
                .id(101L)
                .name("spring boot repository")
                .description("repo for assignment")
                .owner(new GithubApiResponseDTO.Owner("user123"))
                .language("java")
                .stars(500)
                .forks(120)
                .lastUpdated(OffsetDateTime.now())
                .build();

        GithubApiResponseDTO apiResponse = GithubApiResponseDTO.builder()
                .totalCount(1)
                .items(List.of(repoItem))
                .build();

        when(responseSpec.bodyToMono(GithubApiResponseDTO.class))
                .thenReturn(Mono.just(apiResponse));
        when(repoRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        List<RepoResponseDTO> result = githubService.searchAndSaveRepos(request);

        //Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("spring boot repository");
    }

    @Test
    void testSearchAndSaveRepositories_returnsEmptyListWhenNoResults(){
        //Arrange
        GithubSearchRequestDTO request = new GithubSearchRequestDTO(
                "nonExistentRepo","Java","stars");

        GithubApiResponseDTO emptyResponse = GithubApiResponseDTO.builder()
                .totalCount(0).items(List.of()).build();

        when(responseSpec.bodyToMono(GithubApiResponseDTO.class)).thenReturn(
                Mono.just(emptyResponse));

        //Act
        List<RepoResponseDTO> result = githubService.searchAndSaveRepos(request);

        //Assert
        assertThat(result).isEmpty();
    }

    @Test
    void testSearchAndSaveRepositories_throwsGithubApiExceptionOnError(){
        //Arrange
        GithubSearchRequestDTO request = new GithubSearchRequestDTO("spring boot", "java", "stars");
        when(responseSpec.bodyToMono(GithubApiResponseDTO.class))
                .thenThrow(WebClientResponseException.create(500, "Internal Server Error", null, null, null));

        //Act & assert
        assertThatThrownBy(() -> githubService.searchAndSaveRepos(request))
                .isInstanceOf(GithubApiException.class)
                .hasMessageContaining("GitHub API error");
    }

    @Test
    void testGetStoredRepositories_throwsInvalidRequestExceptionWhenStarsNegative(){
        //Act & Assert
        assertThatThrownBy(() -> githubService.getStoredRepos("Java", -5, "stars"))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("minStars must be non-negative");
    }

    @Test
    void testGetStoredRepositories_filtersAndSorts(){
        //Arrange
        RepoEntity repoEntity = RepoEntity.builder()
                .id(1L).name("Repo A").ownerName("AB").language("java")
                .stars(50).forks(5).lastUpdated(OffsetDateTime.now().minusDays(1))
                .build();

        RepoEntity repoEntity2 = RepoEntity.builder()
                .id(2L).name("Repo B").ownerName("Anuradha").language("Java")
                .stars(200).forks(20).lastUpdated(OffsetDateTime.now())
                .build();

        when(repoRepository.findByLanguageIgnoreCaseAndStarsGreaterThanEqual("Java", 100))
                .thenReturn(List.of(repo1, repo2));

        //Act
        List<RepoResponseDTO> result = githubService.getStoredRepos(
                "Java", 100, "stars");

        //Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStars()).isGreaterThanOrEqualTo(result.get(1).getStars());
    }

}