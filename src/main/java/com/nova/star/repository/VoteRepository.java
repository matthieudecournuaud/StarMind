package com.nova.star.repository;

import com.nova.star.domain.Vote;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("select vote from Vote vote where vote.voter.login = ?#{authentication.name}")
    List<Vote> findByVoterIsCurrentUser();
}
