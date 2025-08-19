package dev.anuradha.githubreposearcher.controller;

import dev.anuradha.githubreposearcher.dto.GithubSearchRequestDTO;
import dev.anuradha.githubreposearcher.dto.RepoResponseDTO;
import dev.anuradha.githubreposearcher.service.GithubService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GithubController.class)
@AutoConfigureMockMvc(addFilters = false)
class GithubControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private GithubService githubService;

    @Test
    void testSearchRepositories_returnsJsonResponse() throws Exception{
        RepoResponseDTO mockRepo = RepoResponseDTO.builder()
                .id(1L)
                .name("spring-boot-example")
                .description("Demo repo")
                .owner("user123")
                .language("Java")
                .stars(150)
                .forks(30)
                .lastUpdated(OffsetDateTime.now())
                .build();

        when(githubService.searchAndSaveRepos(any(GithubSearchRequestDTO.class)))
                .thenReturn(List.of(mockRepo));

        GithubSearchRequestDTO request = new GithubSearchRequestDTO(
                "spring boot","java", "stars");

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories[0].name").value("spring-boot-example"))
                .andExpect(jsonPath("$.repositories[0].stars").value(150));
    }

    @Test
    void testGetRepositories_returnsJsonResponse() throws Exception{
        RepoResponseDTO repo1 = RepoResponseDTO.builder()
                .id(2L).name("Repo A").owner("ADB").language("Java")
                .stars(500).forks(50).lastUpdated(OffsetDateTime.now())
                .build();

        RepoResponseDTO repo2 = RepoResponseDTO.builder()
                .id(3L).name("Repo B").owner("Bob").language("java")
                .stars(200).forks(20).lastUpdated(OffsetDateTime.now())
                .build();

        when(githubService.getStoredRepos("Java",100, "stars"))
                .thenReturn(List.of(repo1, repo2));

        mockMvc.perform(get("/api/github/repositories")
                .param("language","Java")
                .param("minStars","100")
                .param("sort", "stars"))
        .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Repo A"))
                .andExpect(jsonPath("$[0].stars").value(500))
                .andExpect(jsonPath("$[1].name").value("Repo B"));
    }

    @Test
    void testGetRepositories_returnsEmptyList() throws Exception{
        when(githubService.getStoredRepos("Python", 50, "stars"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/github/repositories")
                .param("language","Python")
                .param("minStars", "50")
                .param("sort", "stars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}