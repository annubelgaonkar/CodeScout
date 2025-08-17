package dev.anuradha.githubreposearcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RepoResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String ownerName;
    private String language;
    private Integer stars;
    private Integer forks;
    private OffsetDateTime lastUpdated;
}
