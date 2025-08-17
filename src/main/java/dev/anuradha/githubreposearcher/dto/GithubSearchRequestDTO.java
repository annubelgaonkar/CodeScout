package dev.anuradha.githubreposearcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GithubSearchRequestDTO {
    private String query;
    private String language;
    private String sort;
}
