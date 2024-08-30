package com.nova.star.repository;

import com.nova.star.domain.LikeHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LikeHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeHistoryRepository extends JpaRepository<LikeHistory, Long> {}
