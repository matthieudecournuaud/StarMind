package com.nova.star.repository;

import com.nova.star.domain.IdeaHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IdeaHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdeaHistoryRepository extends JpaRepository<IdeaHistory, Long> {}
