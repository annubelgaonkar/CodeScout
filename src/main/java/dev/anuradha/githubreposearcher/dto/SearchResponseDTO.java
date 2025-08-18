package dev.anuradha.githubreposearcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponseDTO {

    private String message;
    private List<RepoResponseDTO> repositories;
}
