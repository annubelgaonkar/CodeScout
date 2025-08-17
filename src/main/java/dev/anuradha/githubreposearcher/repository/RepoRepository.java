package dev.anuradha.githubreposearcher.repository;

import dev.anuradha.githubreposearcher.model.RepoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoRepository extends JpaRepository<RepoEntity, Long> {

    // Find by language
    List<RepoEntity> findByLanguageIgnoreCase(String language);

    // Find by stars greater than or equal to
    List<RepoEntity> findByStarsGreaterThanEqual(Integer stars);

    // Find by language + stars
    List<RepoEntity> findByLanguageIgnoreCaseAndStarsGreaterThanEqual(String language,
                                                                            Integer stars);


}
