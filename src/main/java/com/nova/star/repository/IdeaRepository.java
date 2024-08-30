package com.nova.star.repository;

import com.nova.star.domain.Idea;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Idea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    @Query("select idea from Idea idea where idea.author.login = ?#{authentication.name}")
    List<Idea> findByAuthorIsCurrentUser();
}
