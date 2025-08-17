package dev.anuradha.githubreposearcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GithubApiResponseDTO {

    @JsonProperty("total_count")
    private Integer totalCount;

    private List<RepoItem> items;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RepoItem{
        private Long id;
        private String name;
        private String description;

        private Owner owner;

        private String language;

        @JsonProperty("stargazers_count")
        private Integer stars;

        @JsonProperty("forks_count")
        private Integer forks;

        @JsonProperty("updated_at")
        private OffsetDateTime lastUpdated;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Owner{
        private String login;
    }
}
