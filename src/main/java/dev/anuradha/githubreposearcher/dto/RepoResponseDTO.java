package dev.anuradha.githubreposearcher.dto;

import lombok.*;
import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepoResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String language;
    private Integer stars;
    private Integer forks;
    private OffsetDateTime lastUpdated;
}
