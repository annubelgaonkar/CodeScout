package dev.anuradha.githubreposearcher.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "repositories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepoEntity {

    @Id
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "language")
    private String language;

    @Column(name = "stars_count")
    private Integer stars;

    @Column(name = "forks_count")
    private Integer forks;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

}
